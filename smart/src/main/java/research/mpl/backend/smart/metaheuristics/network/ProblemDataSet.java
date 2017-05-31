package research.mpl.backend.smart.metaheuristics.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

import javax.inject.Inject;

public class ProblemDataSet {
	
	private final int NUM_PATTERNS_CLASS = 4000;//TODO It is not being used
	
	private int numInputs;
	private int numOutputs;
	private int numberOfPatterns;
	
	List<ProblemSample> trainingSet = null;

	DecimalFormat decimalFormat = null;

    @Inject
    private ProblemSampleRepository repository;

    public void init(){
        Locale.setDefault(Locale.US);

        this.trainingSet = new ArrayList<ProblemSample>();

        String format = "###,###.000000";
        Locale ptBR = new Locale("pt", "BR");
		Locale enUS = new Locale("en", "US");
        this.decimalFormat = (DecimalFormat)DecimalFormat.getNumberInstance(enUS);
        //this.decimalFormat = new DecimalFormat( format );

        //decimalFormat.setParseBigDecimal(true);

        //this.decimalFormat.setRoundingMode(roundingMode);
        Properties experimentsProperties = new Properties();
        Properties datasetProperties = new Properties();

        try {
            InputStream experimentsPropertiesIS =
					ProblemDataSet.class.getResourceAsStream("/experiments.properties");
            experimentsProperties.load(experimentsPropertiesIS);
            String datasetpath = experimentsProperties.getProperty("datasetpath");
            String datasetname = experimentsProperties.getProperty("datasetname");


            InputStream dataSetPropertiesIS = new FileInputStream(
                    datasetpath + File.separator + datasetname + "_description.properties");
            datasetProperties.load(dataSetPropertiesIS);
            this.numberOfPatterns = this.decimalFormat
                    .parse(datasetProperties.getProperty("number_of_patterns"))
                    .intValue();
            this.numInputs = this.decimalFormat
                    .parse(datasetProperties.getProperty("number_of_input_variables"))
                    .intValue();
            this.numOutputs = this.decimalFormat
                    .parse(datasetProperties.getProperty("number_of_outputs"))
                    .intValue();

            double minOutpuValue = 0;
            double maxOutpuValue = 99999;

//			readTrainingSetFiles_WineQuality(datasetpath, true);
//			readTrainingSetFiles_BPRegression(datasetpath);
//            loadTrainingSetToDB(datasetpath, true);
            readTrainingSetFromDB();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

	public double calculateErrorInstance(double[] obtainedValue, List<Double> desiredValue) {
		
		double error = 0;
		
		for(int i = 0; i < obtainedValue.length; i++){
			error += Math.pow((obtainedValue[i] - desiredValue.get(i).doubleValue()), 2);
		}
		//error = error/obtainedValue.length;
		
		return error;
	}

	public double calculateUnitOutputError(double obtainedValue, double desiredValue) {
		
		double error = desiredValue - obtainedValue; //Math.pow((obtainedValue - desiredValue), 2);
		return error;
	}
	
	private void addTree(Path directory, final Collection<Path> all)
	        throws IOException {
		
	    Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
	        @Override
	        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
	                throws IOException {
	            all.add(file);
	            return FileVisitResult.CONTINUE;
	        }
	    });
	}
	
	private void readTrainingSetFiles_WineQuality(String datasepath, boolean ignoreFirstLine) {
		System.out.println("Reading Training Set Files...");
		String line = null;
		String[] values = null;
		FileReader fr = null;
		LineNumberReader lnr = null;
		
		Path path = Paths.get(datasepath);
		Collection<Path> files = new Vector<Path>();
		try {
			addTree(path, files);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		READ_SAMPLES_LOOP:
		for (Path filePath : files) {
			if (filePath.getFileName().toString().startsWith("dataset")) {
				
				try {
					fr = new FileReader(filePath.toFile());
					lnr = new LineNumberReader(fr);
					line = lnr.readLine();
					if(ignoreFirstLine) {
						// Ignore header line of csv file.
						line = lnr.readLine();
					}
					while (line != null) {
						
						values = line.split(";");
						double[] inputVariables = new double[numInputs]; 
						for (int i = 0; i < inputVariables.length; i++) {
							inputVariables[i] = decimalFormat.parse(values[i]).doubleValue();
						}
						
						double[] outputVariables = new double[numOutputs];
						for (int i = 0; i < outputVariables.length; i++) {
							outputVariables[i] = decimalFormat.parse(values[numInputs+i]).doubleValue();
						}
	
						this.trainingSet.add(new ProblemSample(inputVariables, outputVariables));
						
						if (this.trainingSet.size() >= this.numberOfPatterns) {
							break READ_SAMPLES_LOOP;
						}
						
						line = lnr.readLine();
					}
	
					fr.close();
					lnr.close();
	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		// Normalize
		double[] minValuesFromInputs = new double[this.numInputs];
		double[] maxValuesFromInputs = new double[this.numInputs];
		Arrays.fill(minValuesFromInputs, Double.MAX_VALUE);
		Arrays.fill(maxValuesFromInputs, Double.MIN_VALUE);
		
		double[] minValuesFromOutputs = new double[this.numOutputs];
		double[] maxValuesFromOutputs = new double[this.numOutputs];
		Arrays.fill(minValuesFromOutputs, Double.MAX_VALUE);
		Arrays.fill(maxValuesFromOutputs, Double.MIN_VALUE);
		
		for (ProblemSample sample : trainingSet) {
			for (int i = 0; i < sample.getInputs().size(); i++) {
				if (sample.getInputValue(i) < minValuesFromInputs[i]) {
					minValuesFromInputs[i] = sample.getInputValue(i);
				}
				if (sample.getInputValue(i) > maxValuesFromInputs[i]) {
					maxValuesFromInputs[i] = sample.getInputValue(i);
				}
			}
			
			for (int i = 0; i < sample.getOutputs().size(); i++) {
				if (sample.getOutputValue(i) < minValuesFromOutputs[i]) {
					minValuesFromOutputs[i] = sample.getOutputValue(i);
				}
				if (sample.getOutputValue(i) > maxValuesFromOutputs[i]) {
					maxValuesFromOutputs[i] = sample.getOutputValue(i);
				}
			}
		}
		
		for (ProblemSample sample : trainingSet) {
			for (int i = 0; i < sample.getInputs().size(); i++) {
				sample.setInputValue(i, (sample.getInputValue(i) - minValuesFromInputs[i]) / (maxValuesFromInputs[i] - minValuesFromInputs[i]));
			}
			
			for (int i = 0; i < sample.getOutputs().size(); i++) {
				sample.setOutputValue(i, (sample.getOutputValue(i) - minValuesFromOutputs[i]) / (maxValuesFromOutputs[i] - minValuesFromOutputs[i]));
			}
		}
		
	}
	
	private void readTrainingSetFiles_BPRegression(String path) {
		
		System.out.println("Reading Training Set Files...");
		String linha = null;
		String[] values = null;

		double pb = 0;
		double w = 0;
		double oxc = 0;
		double algebraicConectivity = 0;
		double naturalConectivity = 0;
		double density = 0;
		double averageDegree = 0;
		double averagePathLength = 0;
		double clusteringCoefficient = 0;
		double diameter = 0;
		double entropy = 0;
		double cost = 0;
		int count = 0;
		
		File dirBase = new File(path);
		
		File subDir = null;
		FileReader fr = null;
		LineNumberReader lnr = null;
		lblExt: for (String dir : dirBase.list()) {
			subDir = new File(dirBase.getAbsolutePath() + File.separator + dir);
			if (subDir.isDirectory()) {
				System.out.println("directory: " + subDir.getName());
				for (String arq : subDir.list()) {
					if (arq.contains("_a_")) {
						continue;
					}
					try {
						System.out.print("file: " + arq + ": ");
						
						fr = new FileReader(new File(subDir.getAbsolutePath() + File.separator + arq));
						lnr = new LineNumberReader(fr);
						linha = lnr.readLine();
						lblInt: while (linha != null) {
							// System.out.println(linha);
							values = linha.split(";");
							cost = decimalFormat.parse(values[0]).doubleValue();
							pb = decimalFormat.parse(values[1]).doubleValue();
							w = decimalFormat.parse(values[2]).doubleValue();
							oxc = decimalFormat.parse(values[3]).doubleValue();
							algebraicConectivity = decimalFormat.parse(values[4]).doubleValue();
							naturalConectivity = decimalFormat.parse(values[5]).doubleValue();
//							density = decimalFormat.parse(values[6]).doubleValue();
//							averageDegree = decimalFormat.parse(values[7]).doubleValue();
							averagePathLength = decimalFormat.parse(values[8]).doubleValue();
							clusteringCoefficient = decimalFormat.parse(values[9]).doubleValue();
							diameter = decimalFormat.parse(values[10]).doubleValue();
							entropy = decimalFormat.parse(values[11]).doubleValue();
							
							double[] inputVariables = new double[] { w, 
																	 oxc,
																	 algebraicConectivity, 
																	 naturalConectivity, 
																	 averagePathLength,
																	 clusteringCoefficient, 
																	 diameter, 
																	 entropy };
							
							double[] outputVariables = new double[] { pb };

							// remove outliers
							if (algebraicConectivity > 0 
									&& diameter > 0 
									&& diameter < (Integer.MAX_VALUE - 1)
									&& cost < 20141 
									&& pb < 1) {
								if (!checkIfExist(inputVariables)) {// do not add if inputs are repeated
    								this.trainingSet.add(new ProblemSample(inputVariables, outputVariables));
    								count++;
    								if (count >= this.numberOfPatterns) {
    									break lblExt;
    								}
								}
							}

							linha = lnr.readLine();
						}
						System.out.println(count);
						fr.close();
						lnr.close();

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
		System.out.println(count);
		System.out.println("Number of Patterns=" + count);
		
//		System.out.println("Before normalization...");
//		for (ProblemSample trainingSample : this.trainingSet) {
//			System.out.println(trainingSample.toString());
//		}
		
		// Normalize
		double[] minValues = new double[this.numInputs];
		double[] maxValues = new double[this.numInputs];
		Arrays.fill(minValues, Double.MAX_VALUE);
		Arrays.fill(maxValues, Double.MIN_VALUE);
		for (ProblemSample sample : trainingSet) {
			for (int i = 0; i < sample.getInputs().size(); i++) {
				if (sample.getInputValue(i) < minValues[i]) {
					minValues[i] = sample.getInputValue(i);
				}
				if (sample.getInputValue(i) > maxValues[i]) {
					maxValues[i] = sample.getInputValue(i);
				}
			}
		}
		for (ProblemSample sample : trainingSet) {
			for (int i = 0; i < sample.getInputs().size(); i++) {
				if(maxValues[i] == minValues[i]){
					sample.setInputValue(i, 1);
				} else {
					sample.setInputValue(i, (sample.getInputValue(i) - minValues[i]) / (maxValues[i] - minValues[i]));			
				}
			}
		}
		
//		System.out.println("After normalization...");
//		for (ProblemSample trainingSample : this.trainingSet) {
//			System.out.println(trainingSample.toString());
//		}
		
		System.out.println("Max/min values of each variable:");
		for (int i = 0; i < minValues.length; i++) {
			System.out.printf("%.4f %.4f\n", minValues[i], maxValues[i]);
		}
		System.out.println();
	}
	
	private boolean checkIfExist(double[] inputVariables){
		boolean exists = false;
		boolean isEqual = true;
		for (ProblemSample problemSample : this.trainingSet) {
			isEqual = true;
			for (int i = 0; i < inputVariables.length; i++) {
				if (problemSample.getInputValue(i) != inputVariables[i]) {
					isEqual = false;
					break;
				}
					
			}
			
			if(isEqual){
				exists = true;
				break;
			}
		}
		return exists;
	}
	
	private void separatePatterns(){
		int numPadroesClasse = NUM_PATTERNS_CLASS;
		List<ProblemSample> padroes1 = new Vector<>();
		List<ProblemSample> padroes2 = new Vector<>();
		List<ProblemSample> padroes3 = new Vector<>();
		List<ProblemSample> padroes4 = new Vector<>();
		
		for (ProblemSample instance : this.trainingSet) {
			if (instance.getOutputs().get(0) > 0.75) {
				padroes4.add(instance);
			} else if (instance.getOutputs().get(0) < 0.25) {
				padroes1.add(instance);
			} else if (instance.getOutputs().get(0) > 0.25 && instance.getOutputs().get(0) < 0.50) {
				padroes2.add(instance);
			} else {
				padroes3.add(instance);
			}
		}
		int menor = padroes4.size();
		if (padroes3.size() < menor) {
			menor = padroes3.size();
		}
		if (padroes2.size() < menor) {
			menor = padroes2.size();
		}
		if (padroes1.size() < menor) {
			menor = padroes1.size();
		}
		if (menor > numPadroesClasse) {
			menor = numPadroesClasse;
		}
		menor /= 2;
		ProblemSample[] padroesTreinamentoClass = new ProblemSample[menor * 4];
		int j = 0;
		int i = 0;
		for (; i < menor; i++) {
			padroesTreinamentoClass[j] = padroes1.get(i);
			padroesTreinamentoClass[j + 1] = padroes2.get(i);
			padroesTreinamentoClass[j + 2] = padroes3.get(i);
			padroesTreinamentoClass[j + 3] = padroes4.get(i);
			j += 4;
		}
		menor /= 2;
		ProblemSample[] padroesValidacaoClass = new ProblemSample[menor * 4];
		j = 0;
		for (; i < menor + padroesTreinamentoClass.length / 4; i++) {
			padroesValidacaoClass[j] = padroes1.get(i);
			padroesValidacaoClass[j + 1] = padroes2.get(i);
			padroesValidacaoClass[j + 2] = padroes3.get(i);
			padroesValidacaoClass[j + 3] = padroes4.get(i);
			j += 4;
		}
		ProblemSample[] padroesTesteClass = new ProblemSample[menor * 4];
		j = 0;
		for (; i < menor + padroesTreinamentoClass.length / 4 + padroesValidacaoClass.length / 4; i++) {
			padroesTesteClass[j] = padroes1.get(i);
			padroesTesteClass[j + 1] = padroes2.get(i);
			padroesTesteClass[j + 2] = padroes3.get(i);
			padroesTesteClass[j + 3] = padroes4.get(i);
			j += 4;
		}
//		rede.treinar(padroesTreinamentoClass, padroesValidacaoClass, padroesTesteClass,
//				padroesTreinamentoClass.length * 50, 0.000001, 0.05, 0.01, strRede);
//		rede.salvarRede(strRede);
	}

    public void loadTrainingSetToDB(String datasepath, boolean ignoreFirstLine){
        System.out.println("Reading Training Set Files...");
        String line = null;
        String[] values = null;
        FileReader fr = null;
        LineNumberReader lnr = null;

        Path path = Paths.get(datasepath);
        Collection<Path> files = new Vector<Path>();
        try {
            addTree(path, files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        READ_SAMPLES_LOOP:
        for (Path filePath : files) {
            if (filePath.getFileName().toString().startsWith("dataset")) {

                try {
                    fr = new FileReader(filePath.toFile());
                    lnr = new LineNumberReader(fr);
                    line = lnr.readLine();
                    if(ignoreFirstLine) {
                        // Ignore header line of csv file.
                        line = lnr.readLine();
                    }
                    while (line != null) {

                        values = line.split(";");
                        double[] inputVariables = new double[numInputs];
                        for (int i = 0; i < inputVariables.length; i++) {
                            inputVariables[i] = decimalFormat.parse(values[i]).doubleValue();
                        }

                        double[] outputVariables = new double[numOutputs];
                        for (int i = 0; i < outputVariables.length; i++) {
                            outputVariables[i] = decimalFormat.parse(values[numInputs+i]).doubleValue();
                        }

                        ProblemSample sample = new ProblemSample(inputVariables, outputVariables);
                        this.trainingSet.add(sample);
                        this.repository.create(sample);

                        if (this.trainingSet.size() >= this.numberOfPatterns) {
                            break READ_SAMPLES_LOOP;
                        }

                        line = lnr.readLine();
                    }

                    fr.close();
                    lnr.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }



    }

    public void readTrainingSetFromDB(){
        trainingSet = this.repository.findAll();

        // Normalize
        double[] minValuesFromInputs = new double[this.numInputs];
        double[] maxValuesFromInputs = new double[this.numInputs];
        Arrays.fill(minValuesFromInputs, Double.MAX_VALUE);
        Arrays.fill(maxValuesFromInputs, Double.MIN_VALUE);

        double[] minValuesFromOutputs = new double[this.numOutputs];
        double[] maxValuesFromOutputs = new double[this.numOutputs];
        Arrays.fill(minValuesFromOutputs, Double.MAX_VALUE);
        Arrays.fill(maxValuesFromOutputs, Double.MIN_VALUE);

        for (ProblemSample sample : trainingSet) {
            for (int i = 0; i < sample.getInputs().size(); i++) {
                if (sample.getInputValue(i) < minValuesFromInputs[i]) {
                    minValuesFromInputs[i] = sample.getInputValue(i);
                }
                if (sample.getInputValue(i) > maxValuesFromInputs[i]) {
                    maxValuesFromInputs[i] = sample.getInputValue(i);
                }
            }

            for (int i = 0; i < sample.getOutputs().size(); i++) {
                if (sample.getOutputValue(i) < minValuesFromOutputs[i]) {
                    minValuesFromOutputs[i] = sample.getOutputValue(i);
                }
                if (sample.getOutputValue(i) > maxValuesFromOutputs[i]) {
                    maxValuesFromOutputs[i] = sample.getOutputValue(i);
                }
            }
        }

        for (ProblemSample sample : trainingSet) {
            for (int i = 0; i < sample.getInputs().size(); i++) {
                sample.setInputValue(i, (sample.getInputValue(i) - minValuesFromInputs[i]) / (maxValuesFromInputs[i] - minValuesFromInputs[i]));
            }

            for (int i = 0; i < sample.getOutputs().size(); i++) {
                sample.setOutputValue(i, (sample.getOutputValue(i) - minValuesFromOutputs[i]) / (maxValuesFromOutputs[i] - minValuesFromOutputs[i]));
            }
        }
    }

    public void saveTrainingSetToDB(){
        //Update
        //Insert
    }

	public void transformTrainingSetDbIntoSingleTable(String tableName){
		List<ProblemSample> list = this.repository.findAll();
		StringBuilder strB = new StringBuilder();
		for (ProblemSample problemSample : list) {
			strB.append(problemSample.getId());
			for (Double input : problemSample.getInputs()) {
				strB.append(input);
			}
			for (Double output : problemSample.getOutputs()) {
				strB.append(output);
			}
		}
	}

	public List<ProblemSample> getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(List<ProblemSample> trainingSet) {
		this.trainingSet = trainingSet;
	}
	
	public int getNumberOfPatterns() {
		return numberOfPatterns;
	}

	public int getNumInputs() {
		return numInputs;
	}
	
	public int getNumOutputs() {
		return numOutputs;
	}
}

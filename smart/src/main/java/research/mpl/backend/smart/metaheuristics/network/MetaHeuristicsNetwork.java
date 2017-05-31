package research.mpl.backend.smart.metaheuristics.network;

import research.mpl.backend.smart.util.EnduranceException;

/**
 * Created by Marilia Portela on 30/04/2017.
 */
public abstract class MetaHeuristicsNetwork {

    public abstract int getNumberOfVariableWeights(boolean updateReservoirLayer);
    public abstract void setNetworkWeights(double[] weights, boolean updateReservoirLayer);
    public abstract void buildNetwork() throws EnduranceException;
    public abstract double[] forwardPropagateSignal(int t, ProblemSample instance);
    public abstract NetworkTopology getNetworkTopology();
    public abstract void printNetworkTopologyToFile(String path);
    public abstract void printNetworkWeightsToFile(String path);
}

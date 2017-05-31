CREATE FUNCTION obterTabela()
RETURNS TABLE (id_tabela INT, sqlquery VARCHAR(1000000))
--RETURNS VARCHAR(1000000)
--MODIFIES SQL DATA
--NOT DETERMINISTIC
BEGIN ATOMIC
DECLARE TABLE tabela (id_tabela INT, sqlquery_update VARCHAR(1000000));
DECLARE num_inputs INT;
DECLARE index_input INT;
DECLARE last_index INT;
DECLARE length_coluna INT;
DECLARE desvio INT;
DECLARE conteudo_coluna VARCHAR(20);
DECLARE listaSqlQuery VARCHAR(1000000);
for_label: FOR SELECT "ID","INPUTS" FROM (
	WITH GROUPED_INPUTS_AND_OUTPUTS AS (
		WITH GROUPED_INPUTS AS (
			select TS.ID,  GROUP_CONCAT(INPUTS SEPARATOR ' ; ') AS INPUTS --,  GROUP_CONCAT(OUTPUTS SEPARATOR ' ; ')
			FROM TRAININGSAMPLE TS
			JOIN TRAININGSAMPLE_INPUTS TSI ON TSI.TRAININGSAMPLE_ID = TS.ID
			GROUP BY TS.ID, TSI.TRAININGSAMPLE_ID
		)
		SELECT ID, INPUTS, GROUP_CONCAT(OUTPUTS SEPARATOR ' ; ')
		FROM GROUPED_INPUTS
		JOIN  TRAININGSAMPLE_OUTPUTS tso ON GROUPED_INPUTS.ID = tso.TRAININGSAMPLE_ID
		GROUP BY ID,INPUTS
	)
	SELECT
	*
	FROM GROUPED_INPUTS_AND_OUTPUTS
	WHERE ID = 1
)
DO
	SET listaSqlQuery = ' CREATE TABLE IF NOT EXISTS temp_table (id_temp INT);';
	SET listaSqlQuery = listaSqlQuery + ' INSERT INTO temp_table (id_temp) VALUES ('+ID+');';
	SET num_inputs = CARDINALITY(REGEXP_SUBSTRING_ARRAY ( INPUTS, ';' )) + 1;
	SET index_input = 0;
	SET last_index = 0;
	SET desvio = 0;
	while_label: WHILE index_input < num_inputs DO
		SET last_index = LOCATE(';',INPUTS, desvio);
		IF last_index = 0 THEN SET last_index = CHARACTER_LENGTH(INPUTS);  END IF;
		SET length_coluna = last_index - desvio;
		--SET listaSqlQuery = listaSqlQuery+ ' desvio=' +desvio+',last_index='+length_coluna + ' '+index_input + ' ' +num_inputs +' '+ INPUTS;
		SET conteudo_coluna = SUBSTRING(INPUTS, desvio, length_coluna);
		SET index_input = index_input + 1;
		SET desvio = last_index + 1;
		SET listaSqlQuery = listaSqlQuery + ' ALTER TABLE temp_table ADD nova_coluna'+index_input+' CHAR(25);';
		SET listaSqlQuery = listaSqlQuery + ' UPDATE temp_table SET nova_coluna'+index_input+' = '+conteudo_coluna +' WHERE id_temp = '+ID+';';
	END WHILE while_label;
	INSERT INTO tabela (id_tabela, sqlquery_update) VALUES (ID, listaSqlQuery);
END FOR for_label;
RETURN TABLE(SELECT * FROM tabela);
--RETURN listaSqlQuery;
END;

DROP FUNCTION IF EXISTS obterTabela

SELECT * FROM TABLE(obterTabela());

CALL(obterTabela());
-------------------------------------


CREATE FUNCTION obterTabela()
RETURNS TABLE (id_tabela INT, sqlquery VARCHAR(1000000))
--RETURNS VARCHAR(1000000)
--MODIFIES SQL DATA
--NOT DETERMINISTIC
BEGIN ATOMIC
DECLARE TABLE tabela (id_tabela INT, sqlquery_update VARCHAR(1000000));
DECLARE num_inputs INT;
DECLARE index_input INT;
DECLARE last_index INT;
DECLARE length_coluna INT;
DECLARE desvio INT;
DECLARE conteudo_coluna VARCHAR(20);
DECLARE listaSqlQuery VARCHAR(1000000);
for_label: FOR SELECT "ID","INPUTS" FROM (
	WITH GROUPED_INPUTS_AND_OUTPUTS AS (
		WITH GROUPED_INPUTS AS (
			select TS.ID,  GROUP_CONCAT(INPUTS SEPARATOR ' ; ') AS INPUTS --,  GROUP_CONCAT(OUTPUTS SEPARATOR ' ; ')
			FROM TRAININGSAMPLE TS
			JOIN TRAININGSAMPLE_INPUTS TSI ON TSI.TRAININGSAMPLE_ID = TS.ID
			GROUP BY TS.ID, TSI.TRAININGSAMPLE_ID
		)
		SELECT ID, INPUTS, GROUP_CONCAT(OUTPUTS SEPARATOR ' ; ')
		FROM GROUPED_INPUTS
		JOIN  TRAININGSAMPLE_OUTPUTS tso ON GROUPED_INPUTS.ID = tso.TRAININGSAMPLE_ID
		GROUP BY ID,INPUTS
	)
	SELECT
	*
	FROM GROUPED_INPUTS_AND_OUTPUTS
	WHERE ID = 1
)
DO
	SET listaSqlQuery = ' CREATE TABLE IF NOT EXISTS temp_table (id_temp INT);';
	SET listaSqlQuery = listaSqlQuery + ' INSERT INTO temp_table (id_temp) VALUES ('+ID+');';
	SET num_inputs = CARDINALITY(REGEXP_SUBSTRING_ARRAY ( INPUTS, ';' )) + 1;
	SET index_input = 0;
	SET last_index = 0;
	SET desvio = 0;
	while_label: WHILE index_input < num_inputs DO
		SET last_index = LOCATE(';',INPUTS, desvio);
		IF last_index = 0 THEN SET last_index = CHARACTER_LENGTH(INPUTS);  END IF;
		SET length_coluna = last_index - desvio;
		--SET listaSqlQuery = listaSqlQuery+ ' desvio=' +desvio+',last_index='+length_coluna + ' '+index_input + ' ' +num_inputs +' '+ INPUTS;
		SET conteudo_coluna = SUBSTRING(INPUTS, desvio, length_coluna);
		SET index_input = index_input + 1;
		SET desvio = last_index + 1;
		SET listaSqlQuery = listaSqlQuery + ' ALTER TABLE temp_table ADD nova_coluna'+index_input+' CHAR(25);';
		SET listaSqlQuery = listaSqlQuery + ' UPDATE temp_table SET nova_coluna'+index_input+' = '+conteudo_coluna +' WHERE id_temp = '+ID+';';
	END WHILE while_label;
	INSERT INTO tabela (id_tabela, sqlquery_update) VALUES (ID, listaSqlQuery);
END FOR for_label;
RETURN TABLE(SELECT * FROM tabela);
--RETURN listaSqlQuery;
END;

DROP FUNCTION IF EXISTS obterTabela

SELECT * FROM TABLE(obterTabela());

CALL(obterTabela());




DROP TABLE IF EXISTS HOJE;

CALL execute_immediate(
'CREATE TABLE IF NOT EXISTS public.HOJEA (id_temp INT);',
--'jdbc:default:connection',
'jdbc:hsqldb:file:D:/OneDrive/Workspaces/WorkspaceIDEA_LAPTOP/endurance/smart/target/database/bullet-db',
'root',
'root')


CREATE PROCEDURE execute_immediate(IN P1 VARCHAR(1000000),IN P2 VARCHAR(500),IN P3 VARCHAR(500),IN P4  VARCHAR (500) )
LANGUAGE JAVA DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME 'CLASSPATH:JdbcSquirrel.executeImmediate';

DROP PROCEDURE IF EXISTS execute_immediate

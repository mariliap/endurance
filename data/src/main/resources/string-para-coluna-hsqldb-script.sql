DROP PROCEDURE IF EXISTS criarTabela

CALL criarTabela();

CREATE PROCEDURE criarTabela()
--RETURNS TABLE (id_tabela INT, sqlquery VARCHAR(1000000))
--RETURNS VARCHAR(1000000)
MODIFIES SQL DATA
--NOT DETERMINISTIC
BEGIN ATOMIC
DECLARE TABLE tabela (id_tabela INT, sqlquery_update VARCHAR(1000000));
DECLARE num_inputs INT;
DECLARE index_input INT;
DECLARE last_index INT;
DECLARE length_coluna INT;
DECLARE desvio INT;
DECLARE conteudo_coluna VARCHAR(20);
DECLARE nome_tabela VARCHAR(50);
DECLARE nome_tabela_teste VARCHAR(50);
DECLARE listaSqlQuery VARCHAR(1000000);
SET nome_tabela = 'dados';
SET nome_tabela_teste = 'dadosteste';
SET listaSqlQuery = ' CREATE TABLE IF NOT EXISTS '+ nome_tabela +' (id_'+nome_tabela+' INT);';
CALL execute_immediate(
'DROP TABLE IF EXISTS '+ nome_tabela+';',
'jdbc:hsqldb:file:D:/OneDrive/Workspaces/WorkspaceIDEA_LAPTOP/endurance/smart/target/database/bullet-db',
'root',
'root');
CALL execute_immediate(
'CREATE TABLE IF NOT EXISTS '+ nome_tabela +' (id_'+nome_tabela+' INT);',
'jdbc:hsqldb:file:D:/OneDrive/Workspaces/WorkspaceIDEA_LAPTOP/endurance/smart/target/database/bullet-db',
'root',
'root');
CALL execute_immediate(
'DROP TABLE IF EXISTS '+ nome_tabela_teste+';',
'jdbc:hsqldb:file:D:/OneDrive/Workspaces/WorkspaceIDEA_LAPTOP/endurance/smart/target/database/bullet-db',
'root',
'root');
CALL execute_immediate(
'CREATE TABLE '+ nome_tabela_teste +' (id_tabela INT, sqlquery_update VARCHAR(1000000);',
'jdbc:hsqldb:file:D:/OneDrive/Workspaces/WorkspaceIDEA_LAPTOP/endurance/smart/target/database/bullet-db',
'root',
'root');
for_label: FOR SELECT "ID","INPUTS" FROM (
	WITH GROUPED_INPUTS_AND_OUTPUTS AS (
		WITH GROUPED_INPUTS AS (
			SELECT TS.ID,  GROUP_CONCAT(INPUTS SEPARATOR ' ; ') AS INPUTS --,  GROUP_CONCAT(OUTPUTS SEPARATOR ' ; ')
			FROM PROBLEMSAMPLE TS
			JOIN PROBLEMSAMPLE_INPUTS TSI ON TSI.PROBLEMSAMPLE_ID = TS.ID
			GROUP BY TS.ID, TSI.PROBLEMSAMPLE_ID
		)
		SELECT ID, INPUTS, GROUP_CONCAT(OUTPUTS SEPARATOR ' ; ') AS OUTPUTS
		FROM GROUPED_INPUTS
		JOIN  PROBLEMSAMPLE_OUTPUTS tso ON GROUPED_INPUTS.ID = tso.PROBLEMSAMPLE_ID
		GROUP BY ID,INPUTS
	)
	SELECT
	*
	FROM GROUPED_INPUTS_AND_OUTPUTS
	WHERE ID IN (1,2,3,4,6)
)
DO
	SET listaSqlQuery = listaSqlQuery + ' INSERT INTO '+nome_tabela+' (id_'+nome_tabela+') VALUES ('+ID+');';
	CALL execute_immediate(
	'INSERT INTO '+nome_tabela+' (id_'+nome_tabela+') VALUES ('+ID+');',
	'jdbc:hsqldb:file:D:/OneDrive/Workspaces/WorkspaceIDEA_LAPTOP/endurance/smart/target/database/bullet-db',
	'root',
	'root');
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
		SET listaSqlQuery = listaSqlQuery + ' ALTER TABLE '+nome_tabela+' ADD coluna'+index_input+' CHAR(25);';
		CALL execute_immediate(
		'ALTER TABLE '+nome_tabela+' ADD coluna'+index_input+' CHAR(25);',
		'jdbc:hsqldb:file:D:/OneDrive/Workspaces/WorkspaceIDEA_LAPTOP/endurance/smart/target/database/bullet-db',
		'root',
		'root');
		SET listaSqlQuery = listaSqlQuery + ' UPDATE '+nome_tabela+' SET coluna'+index_input+' = '+conteudo_coluna +' WHERE id_'+nome_tabela+' = '+ID+';';
		CALL execute_immediate(
		'UPDATE '+nome_tabela+' SET coluna'+index_input+' = '+conteudo_coluna +' WHERE id_'+nome_tabela+' = '+ID+';',
		'jdbc:hsqldb:file:D:/OneDrive/Workspaces/WorkspaceIDEA_LAPTOP/endurance/smart/target/database/bullet-db',
		'root',
		'root');
	END WHILE while_label;
	INSERT INTO tabela (id_tabela, sqlquery_update) VALUES (ID, listaSqlQuery);
	CALL execute_immediate(
	'INSERT INTO '+nome_tabela_teste+' (id_tabela, sqlquery_update) VALUES (' +ID+ ',' +listaSqlQuery+ ');',
	'jdbc:hsqldb:file:D:/OneDrive/Workspaces/WorkspaceIDEA_LAPTOP/endurance/smart/target/database/bullet-db',
	'root',
	'root');
END FOR for_label;
--RETURN TABLE(SELECT * FROM tabela);
--RETURN listaSqlQuery;
END;


WITH GROUPED_INPUTS_AND_OUTPUTS AS (
		WITH GROUPED_INPUTS AS (
			SELECT TS.ID,  GROUP_CONCAT(INPUTS SEPARATOR ' ; ') AS INPUTS --,  GROUP_CONCAT(OUTPUTS SEPARATOR ' ; ')
			FROM PROBLEMSAMPLE TS
			JOIN PROBLEMSAMPLE_INPUTS TSI ON TSI.PROBLEMSAMPLE_ID = TS.ID
			GROUP BY TS.ID, TSI.PROBLEMSAMPLE_ID
		)
		SELECT ID, INPUTS, GROUP_CONCAT(OUTPUTS SEPARATOR ' ; ') AS OUTPUTS
		FROM GROUPED_INPUTS
		JOIN  PROBLEMSAMPLE_OUTPUTS tso ON GROUPED_INPUTS.ID = tso.PROBLEMSAMPLE_ID
		GROUP BY ID,INPUTS
	)
	SELECT
	*
	FROM GROUPED_INPUTS_AND_OUTPUTS
	WHERE ID IN (1,2,3,4,6)

SELECT * FROM DADOS_TESTE
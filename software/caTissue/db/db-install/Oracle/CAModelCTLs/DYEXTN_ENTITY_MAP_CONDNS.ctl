LOAD DATA INFILE 'H://caTissue//work//workspace//catissuecoreNew/SQL/DBUpgrade/Common/CAModelCSVs/DYEXTN_ENTITY_MAP_CONDNS.csv' 
APPEND 
INTO TABLE DYEXTN_ENTITY_MAP_CONDNS 
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
(IDENTIFIER NULLIF IDENTIFIER='\\N',STATIC_RECORD_ID NULLIF STATIC_RECORD_ID='\\N',TYPE_ID NULLIF TYPE_ID='\\N',FORM_CONTEXT_ID NULLIF FORM_CONTEXT_ID='\\N')
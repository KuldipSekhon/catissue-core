LOAD DATA INFILE 'H://caTissue//work//workspace//catissuecoreNew/SQL/DBUpgrade/Common/CAModelCSVs/DYEXTN_ASSO_DISPLAY_ATTR.csv' 
APPEND 
INTO TABLE DYEXTN_ASSO_DISPLAY_ATTR 
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
(IDENTIFIER NULLIF IDENTIFIER='\\N',SEQUENCE_NUMBER NULLIF SEQUENCE_NUMBER='\\N',DISPLAY_ATTRIBUTE_ID NULLIF DISPLAY_ATTRIBUTE_ID='\\N',SELECT_CONTROL_ID NULLIF SELECT_CONTROL_ID='\\N')
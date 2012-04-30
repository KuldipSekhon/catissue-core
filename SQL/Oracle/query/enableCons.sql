-- Enabling constraints--
/* File:query_schema */
--alter table QUERY_PARAMETERIZED_QUERY enable constraint FKA272176BBC7298A9 ;
alter table CATEGORIAL_CLASS enable constraint FK9651EF32D8D56A33 ;
/*alter table CATEGORIAL_CLASS enable constraint FK9651EF3291B ; */
--alter table QUERY_INTRA_MODEL_ASSOCIATION enable constraint FKF1EDBDD3BC7298A9 ;
--alter table QUERY_CONSTRAINTS enable constraint FKE364FCFFD3C625EA ;
--alter table QUERY_PARAMETERIZED_CONDITION enable constraint FK9BE75A3EBC7298A9 ;
--alter table QUERY_CONDITION enable constraint FKACCE6246104CA7 ;
--alter table QUERY_RULE enable constraint FK14A6503365F8F4CB ;
--alter table QUERY_RULE enable constraint FK14A65033BC7298A9 ;
--alter table QUERY enable constraint FK49D20A8251EDC5B ;
--alter table QUERY_LOGICAL_CONNECTOR enable constraint FKCF30478065F8F4CB ;
--alter table QUERY_EXPRESSION enable constraint FK1B473A8FCA571190 ;
--alter table QUERY_EXPRESSION enable constraint FK1B473A8F9E19EF66 ;
--alter table QUERY_EXPRESSION enable constraint FK1B473A8F1CF7F689 ;
--alter table QUERY_CONDITION_VALUES enable constraint FK9997379DDA532516 ;
alter table CATEGORIAL_ATTRIBUTE enable constraint FK31F77B56E8CBD948 ;
--alter table QUERY_INTER_MODEL_ASSOCIATION enable constraint FKD70658D1BC7298A9 ;
--alter table QUERY_EXPRESSION_OPERAND enable constraint FKA3B976F965F8F4CB ;
--alter table QUERY_GRAPH_ENTRY enable constraint FKF055E4EA955C60E6 ;
--alter table QUERY_GRAPH_ENTRY enable constraint FKF055E4EAD3C625EA ;
---alter table QUERY_GRAPH_ENTRY enable constraint FKF055E4EA7A5E6479 ;
--alter table QUERY_GRAPH_ENTRY enable constraint FKF055E4EAEE560703 ;
alter table CATEGORY enable constraint FK31A8ACFE2D0F63E7 ;
alter table CATEGORY enable constraint FK31A8ACFE211D9A6B ;
--alter table QUERY_EXPRESSIONID enable constraint FK6662DBEABC7298A9 ;

/* File:dynamicextension_AddConstraintsNew.sql */


alter table DYEXTN_OBJECT_TYPE_INFO enable constraint FK74819FB0BC7298A9 ;
alter table DYEXTN_COLUMN_PROPERTIES enable constraint FK8FCE2B3FBC7298A9 ;
alter table DYEXTN_COLUMN_PROPERTIES enable constraint FK8FCE2B3FB4C15A36;
alter table DYEXTN_USERDEFINED_DE enable constraint FK630761FFBC7298A9 ;
alter table DYEXTN_CONSTRAINT_PROPERTIES enable constraint FK82886CD8BC7298A9 ;
alter table DYEXTN_CONSTRAINT_PROPERTIES enable constraint FK82886CD8927B15B9 ;
alter table DYEXTN_PERMISSIBLE_VALUE enable constraint FK136264E08C8D972A ;
alter table DYEXTN_PERMISSIBLE_VALUE enable constraint FK136264E03D51114B ;
alter table DYEXTN_CHECK_BOX enable constraint FK4EFF9257BC7298A9 ;
alter table DYEXTN_TABLE_PROPERTIES enable constraint FKE608E08179F466F7 ;
alter table DYEXTN_TABLE_PROPERTIES enable constraint FKE608E081BC7298A9 ;
alter table DYEXTN_ENTITY_MAP_CONDNS enable constraint FK2A9D602969F17C26 ;
alter table DYEXTN_INTEGER_CONCEPT_VALUE enable constraint FKFBA33B3CBC7298A9 ;
alter table DYEXTN_STRING_CONCEPT_VALUE enable constraint FKADE7D889BC7298A9 ;
alter table DYEXTN_BOOLEAN_TYPE_INFO enable constraint FK28F1809FBC7298A9 ;
alter table DYEXTN_BOOLEAN_CONCEPT_VALUE enable constraint FK57B6C4A6BC7298A9 ;
alter table DE_FILE_ATTR_RECORD_VALUES enable constraint FKE68334E7E150DFC9 ;
alter table DYEXTN_STRING_TYPE_INFO enable constraint FKDA35FE02BC7298A9 ;
alter table DYEXTN_SHORT_CONCEPT_VALUE enable constraint FKC1945ABABC7298A9 ;
alter table DYEXTN_FLOAT_TYPE_INFO enable constraint FK7E1C0693BC7298A9 ;
alter table DYEXTN_FILE_UPLOAD enable constraint FK2FAD41E7BC7298A9 ;
alter table DYEXTN_BARR_CONCEPT_VALUE enable constraint FK89D27DF7BC7298A9 ;
alter table DYEXTN_ENTITY enable constraint FK8B243640450711A2 ;
alter table DYEXTN_ENTITY enable constraint FK8B243640BC7298A9 ;
alter table DYEXTN_INTEGER_TYPE_INFO enable constraint FK5F9CB235BC7298A9 ;
alter table DYEXTN_TAGGED_VALUE enable constraint FKF79D055B7D7A9B8E ;
alter table DE_OBJECT_ATTR_RECORD_VALUES enable constraint FK504EADC4E150DFC9 ;
alter table DYEXTN_COMBOBOX enable constraint FKABBC649ABC7298A9 ;
alter table DYEXTN_CADSR_VALUE_DOMAIN_INFO enable constraint FK1C9AA364B4C15A36 ;
alter table DYEXTN_PRIMITIVE_ATTRIBUTE enable constraint FKA9F765C7BC7298A9 ;
alter table DYEXTN_ASSOCIATION enable constraint FK104684243AC5160 ;
alter table DYEXTN_ASSOCIATION enable constraint FK10468424F60C84D6 ;
alter table DYEXTN_ASSOCIATION enable constraint FK104684246315C5C9 ;
alter table DYEXTN_ASSOCIATION enable constraint FK10468424BC7298A9 ;
alter table DYEXTN_CADSRDE enable constraint FK588A2509BC7298A9 ;
alter table DYEXTN_DOUBLE_TYPE_INFO enable constraint FKC83869C2BC7298A9 ;
alter table DYEXTN_SELECT_CONTROL enable constraint FKDFEBB657BC7298A9 ;
alter table DYEXTN_ENTITY_GROUP_REL enable constraint FK5A0D835A992A67D7 ;
alter table DYEXTN_ENTITY_GROUP_REL enable constraint FK5A0D835A79F466F7 ;
alter table DYEXTN_FILE_TYPE_INFO enable constraint FKA00F0EDBC7298A9 ;
alter table DYEXTN_FILE_EXTENSIONS enable constraint FKD49834FA4D87D1BE ;
alter table DYEXTN_LONG_TYPE_INFO enable constraint FK257281EDBC7298A9 ;
alter table DYEXTN_FLOAT_CONCEPT_VALUE enable constraint FK6785309ABC7298A9 ;
alter table DYEXTN_TEXTFIELD enable constraint FKF9AFC850BC7298A9 ;
alter table DYEXTN_RULE enable constraint FKC27E0994D87D1BE ;
alter table DYEXTN_DATE_TYPE_INFO enable constraint FKFBA549FBC7298A9 ;
alter table DYEXTN_ATTRIBUTE_TYPE_INFO enable constraint FK62596D53B4C15A36 ;
alter table DYEXTN_ENTITY_MAP_RECORD enable constraint FK43A4501369F17C26 ;
alter table DYEXTN_ASSO_DISPLAY_ATTR enable constraint FKD12FD3823B3AAE3B ;
alter table DYEXTN_ASSO_DISPLAY_ATTR enable constraint FKD12FD382F7AA8E80 ;
alter table DYEXTN_TEXTAREA enable constraint FK946EE257BC7298A9 ;
alter table DYEXTN_ENTITY_GROUP enable constraint FK105DE7A0BC7298A9 ;
alter table DYEXTN_DATA_ELEMENT enable constraint FKB1153E48C8D972A ;
alter table DYEXTN_DOUBLE_CONCEPT_VALUE enable constraint FKB94E6449BC7298A9 ;
alter table DYEXTN_LIST_BOX enable constraint FK208395A7BC7298A9 ;
alter table DYEXTN_CONTAINMENT_CONTROL enable constraint FK3F9D4AD3F7798636 ;
alter table DYEXTN_CONTAINMENT_CONTROL enable constraint FK3F9D4AD3BC7298A9 ;
alter table DYEXTN_CONTROL enable constraint FK70FB5E80A67822BB ;
alter table DYEXTN_CONTROL enable constraint FK70FB5E809C6A9B9 ;
alter table DYEXTN_LONG_CONCEPT_VALUE enable constraint FK3E1A6EF4BC7298A9 ;
alter table DYEXTN_FORM_CONTEXT enable constraint FKE56CCDB111B8FADA ;
alter table DYEXTN_ATTRIBUTE enable constraint FK37F1E2FFB15CD09F;
alter table DYEXTN_ATTRIBUTE enable constraint FK37F1E2FFBC7298A9 ;
alter table DYEXTN_RULE_PARAMETER enable constraint FK22567363871AAD3E ;
alter table DYEXTN_DATE_CONCEPT_VALUE enable constraint FK45F598A6BC7298A9 ;
alter table DYEXTN_BYTE_ARRAY_TYPE_INFO enable constraint FK18BDA73BC7298A9;
alter table DYEXTN_SEMANTIC_PROPERTY enable constraint FKD2A0B5B13BAB5E46 ;
alter table DYEXTN_SEMANTIC_PROPERTY enable constraint FKD2A0B5B17D7A9B8E ;
alter table DYEXTN_ATTRIBUTE_RECORD enable constraint FK9B20ED9179F466F7 ;
alter table DYEXTN_ATTRIBUTE_RECORD enable constraint FK9B20ED914D87D1BE ;
alter table DYEXTN_DATA_GRID enable constraint FK233EB73EBC7298A9 ;
alter table DE_COLL_ATTR_RECORD_VALUES enable constraint FK847DA577355836BC ;
alter table DYEXTN_NUMERIC_TYPE_INFO enable constraint FK4DEC9544BC7298A9 ;
alter table DYEXTN_SHORT_TYPE_INFO enable constraint FK99540B3BC7298A9;
alter table DYEXTN_CONTAINER enable constraint FK1EAB84E4A1257067 ;
alter table DYEXTN_CONTAINER enable constraint FK1EAB84E479F466F7 ;
alter table DYEXTN_CONTAINER enable constraint FK1EAB84E4992A67D7 ;
alter table DYEXTN_CONTAINER enable constraint FK1EAB84E445DEFCF5 ;
alter table DYEXTN_RADIOBUTTON enable constraint FK16F5BA90BC7298A9 ;
alter table DYEXTN_DATEPICKER enable constraint FKFEADD199BC7298A9 ;



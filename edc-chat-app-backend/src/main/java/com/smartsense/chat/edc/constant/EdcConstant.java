package com.smartsense.chat.edc.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EdcConstant {

    public static final String ID = "@id";
    public static final String CONTEXT = "@context";
    public static final String EDC = "edc";
    public static final String EDC_NS_URL = "https://w3id.org/edc/v0.0.1/ns/";
    public static final String DCT = "dct";
    public static final String DCT_URL = "https://purl.org/dc/terms/";
    public static final String TYPE = "@type";
    public static final String COUNTER_PARTY_ADDRESS = "counterPartyAddress";
    public static final String COUNTER_PARTY_ID = "counterPartyId";
    public static final String PROTOCOL = "protocol";
    public static final String DATASPACE_PROTOCOL = "dataspace-protocol-http";
    public static final String FILTER_EXPRESSION = "filterExpression";
    public static final String OPERAND_LEFT = "operandLeft";
    public static final String OPERATOR = "operator";
    public static final String EQUAL = "=";
    public static final String OPERAND_RIGHT = "operandRight";
    public static final String PERMISSION = "permission";
    public static final String ACTION = "action";
    public static final String USE = "use";
    public static final String TRACTUS_POLICY_URL = "https://w3id.org/tractusx/policy/v1.0.0";
    public static final String ODRL_JSONLD_URL = "http://www.w3.org/ns/odrl.jsonld";
    public static final String VOCAB = "@vocab";
    public static final String S_TYPE = "type";
    public static final String S_TRUE = "true";

    public static final String AGREEMENT_STATE_FINALIZED = "FINALIZED";
    public static final String AGREEMENT_STATE = "state";
    public static final String NEGOTIATION_ID = "NegotiationId";
    public static final String AGREEMENT_ID = "AgreementId";
    public static final String TRANSFER_ID = "TransferId";
}

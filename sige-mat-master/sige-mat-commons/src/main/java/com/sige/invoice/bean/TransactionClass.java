package com.sige.invoice.bean;


import java.math.BigInteger;

public class TransactionClass {
	public static class Header {
		private String ecoreTransactionUUID;
		private BigInteger ecoreTransactionDate;
		private Integer millis;
		public Header() {
			
		}
		public String getEcoreTransactionUUID() {
			return ecoreTransactionUUID;
		}
		public void setEcoreTransactionUUID(String ecoreTransactionUUID) {
			this.ecoreTransactionUUID = ecoreTransactionUUID;
		}
		public BigInteger getEcoreTransactionDate() {
			return ecoreTransactionDate;
		}
		public void setEcoreTransactionDate(BigInteger ecoreTransactionDate) {
			this.ecoreTransactionDate = ecoreTransactionDate;
		}
		public Integer getMillis() {
			return millis;
		}
		public void setMillis(Integer millis) {
			this.millis = millis;
		}
	}
	
	public static class Fullfillment {
		private String channel;
		private String merchantId;
		private String terminalId;
		private String captureType;
		private Boolean countable;
		private Boolean fastPayment;
		private String signature;
		public Fullfillment() {
			
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public String getMerchantId() {
			return merchantId;
		}
		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}
		public String getTerminalId() {
			return terminalId;
		}
		public void setTerminalId(String terminalId) {
			this.terminalId = terminalId;
		}
		public String getCaptureType() {
			return captureType;
		}
		public void setCaptureType(String captureType) {
			this.captureType = captureType;
		}
		public Boolean getCountable() {
			return countable;
		}
		public void setCountable(Boolean countable) {
			this.countable = countable;
		}
		public Boolean getFastPayment() {
			return fastPayment;
		}
		public void setFastPayment(Boolean fastPayment) {
			this.fastPayment = fastPayment;
		}
		public String getSignature() {
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
		}
	}
	
	public static class Order {
		private String tokenId;
		private String purchaseNumber;
		private Double amount;
		private String currency;
		private Double authorizedAmount;
		private String authorizationCode;
		private String actionCode;
		private String traceNumber;
		private String transactionDate;
		private String transactionId;
		public Order () {
			
		}
		public String getTokenId() {
			return tokenId;
		}
		public void setTokenId(String tokenId) {
			this.tokenId = tokenId;
		}
		public String getPurchaseNumber() {
			return purchaseNumber;
		}
		public void setPurchaseNumber(String purchaseNumber) {
			this.purchaseNumber = purchaseNumber;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public Double getAuthorizedAmount() {
			return authorizedAmount;
		}
		public void setAuthorizedAmount(Double authorizedAmount) {
			this.authorizedAmount = authorizedAmount;
		}
		public String getAuthorizationCode() {
			return authorizationCode;
		}
		public void setAuthorizationCode(String authorizationCode) {
			this.authorizationCode = authorizationCode;
		}
		public String getActionCode() {
			return actionCode;
		}
		public void setActionCode(String actionCode) {
			this.actionCode = actionCode;
		}
		public String getTraceNumber() {
			return traceNumber;
		}
		public void setTraceNumber(String traceNumber) {
			this.traceNumber = traceNumber;
		}
		public String getTransactionDate() {
			return transactionDate;
		}
		public void setTransactionDate(String transactionDate) {
			this.transactionDate = transactionDate;
		}
		public String getTransactionId() {
			return transactionId;
		}
		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}
	}
	
	public static class DataMap {
		private String CURRENCY;
		private String TERMINAL;
		private String TRANSACTION_DATE;
		private String ACTION_CODE;
		private String TRACE_NUMBER;
		private String ECI_DESCRIPTION;
		private String ECI;
		private String ID_RESOLUTOR;
		private String SIGNATURE;
		private String CARD;
		private String MERCHANT;
		private String BRAND;
		private String STATUS;
		private String ACTION_DESCRIPTION;
		private String ADQUIRIENTE;
		private String ID_UNICO;
		private String AMOUNT;
		private String PROCESS_CODE;
		private String TRANSACTION_ID;
		private String AUTHORIZATION_CODE;
		public DataMap() {
			
		}
		public String getCURRENCY() {
			return CURRENCY;
		}
		public void setCURRENCY(String cURRENCY) {
			CURRENCY = cURRENCY;
		}
		public String getTERMINAL() {
			return TERMINAL;
		}
		public void setTERMINAL(String tERMINAL) {
			TERMINAL = tERMINAL;
		}
		public String getTRANSACTION_DATE() {
			return TRANSACTION_DATE;
		}
		public void setTRANSACTION_DATE(String tRANSACTION_DATE) {
			TRANSACTION_DATE = tRANSACTION_DATE;
		}
		public String getACTION_CODE() {
			return ACTION_CODE;
		}
		public void setACTION_CODE(String aCTION_CODE) {
			ACTION_CODE = aCTION_CODE;
		}
		public String getTRACE_NUMBER() {
			return TRACE_NUMBER;
		}
		public void setTRACE_NUMBER(String tRACE_NUMBER) {
			TRACE_NUMBER = tRACE_NUMBER;
		}
		public String getECI_DESCRIPTION() {
			return ECI_DESCRIPTION;
		}
		public void setECI_DESCRIPTION(String eCI_DESCRIPTION) {
			ECI_DESCRIPTION = eCI_DESCRIPTION;
		}
		public String getECI() {
			return ECI;
		}
		public void setECI(String eCI) {
			ECI = eCI;
		}
		public String getID_RESOLUTOR() {
			return ID_RESOLUTOR;
		}
		public void setID_RESOLUTOR(String iD_RESOLUTOR) {
			ID_RESOLUTOR = iD_RESOLUTOR;
		}
		public String getSIGNATURE() {
			return SIGNATURE;
		}
		public void setSIGNATURE(String sIGNATURE) {
			SIGNATURE = sIGNATURE;
		}
		public String getCARD() {
			return CARD;
		}
		public void setCARD(String cARD) {
			CARD = cARD;
		}
		public String getMERCHANT() {
			return MERCHANT;
		}
		public void setMERCHANT(String mERCHANT) {
			MERCHANT = mERCHANT;
		}
		public String getBRAND() {
			return BRAND;
		}
		public void setBRAND(String bRAND) {
			BRAND = bRAND;
		}
		public String getSTATUS() {
			return STATUS;
		}
		public void setSTATUS(String sTATUS) {
			STATUS = sTATUS;
		}
		public String getACTION_DESCRIPTION() {
			return ACTION_DESCRIPTION;
		}
		public void setACTION_DESCRIPTION(String aCTION_DESCRIPTION) {
			ACTION_DESCRIPTION = aCTION_DESCRIPTION;
		}
		public String getADQUIRIENTE() {
			return ADQUIRIENTE;
		}
		public void setADQUIRIENTE(String aDQUIRIENTE) {
			ADQUIRIENTE = aDQUIRIENTE;
		}
		public String getID_UNICO() {
			return ID_UNICO;
		}
		public void setID_UNICO(String iD_UNICO) {
			ID_UNICO = iD_UNICO;
		}
		public String getAMOUNT() {
			return AMOUNT;
		}
		public void setAMOUNT(String aMOUNT) {
			AMOUNT = aMOUNT;
		}
		public String getPROCESS_CODE() {
			return PROCESS_CODE;
		}
		public void setPROCESS_CODE(String pROCESS_CODE) {
			PROCESS_CODE = pROCESS_CODE;
		}
		public String getTRANSACTION_ID() {
			return TRANSACTION_ID;
		}
		public void setTRANSACTION_ID(String tRANSACTION_ID) {
			TRANSACTION_ID = tRANSACTION_ID;
		}
		public String getAUTHORIZATION_CODE() {
			return AUTHORIZATION_CODE;
		}
		public void setAUTHORIZATION_CODE(String aUTHORIZATION_CODE) {
			AUTHORIZATION_CODE = aUTHORIZATION_CODE;
		}
	}
	
	public static class Data {
		private String CURRENCY;
		private String TRANSACTION_DATE;
		private String ACTION_CODE;
		private String STATUS;
		private String ACTION_DESCRIPTION;
		private String TRACE_NUMBER;
		private String AMOUNT;
		private String SIGNATURE;
		private String BRAND;
		private String MERCHANT;
		public Data () {
			
		}
		public String getCURRENCY() {
			return CURRENCY;
		}
		public void setCURRENCY(String cURRENCY) {
			CURRENCY = cURRENCY;
		}
		public String getTRANSACTION_DATE() {
			return TRANSACTION_DATE;
		}
		public void setTRANSACTION_DATE(String tRANSACTION_DATE) {
			TRANSACTION_DATE = tRANSACTION_DATE;
		}
		public String getACTION_CODE() {
			return ACTION_CODE;
		}
		public void setACTION_CODE(String aCTION_CODE) {
			ACTION_CODE = aCTION_CODE;
		}
		public String getSTATUS() {
			return STATUS;
		}
		public void setSTATUS(String sTATUS) {
			STATUS = sTATUS;
		}
		public String getACTION_DESCRIPTION() {
			return ACTION_DESCRIPTION;
		}
		public void setACTION_DESCRIPTION(String aCTION_DESCRIPTION) {
			ACTION_DESCRIPTION = aCTION_DESCRIPTION;
		}
		public String getTRACE_NUMBER() {
			return TRACE_NUMBER;
		}
		public void setTRACE_NUMBER(String tRACE_NUMBER) {
			TRACE_NUMBER = tRACE_NUMBER;
		}
		public String getAMOUNT() {
			return AMOUNT;
		}
		public void setAMOUNT(String aMOUNT) {
			AMOUNT = aMOUNT;
		}
		public String getSIGNATURE() {
			return SIGNATURE;
		}
		public void setSIGNATURE(String sIGNATURE) {
			SIGNATURE = sIGNATURE;
		}
		public String getBRAND() {
			return BRAND;
		}
		public void setBRAND(String bRAND) {
			BRAND = bRAND;
		}
		public String getMERCHANT() {
			return MERCHANT;
		}
		public void setMERCHANT(String mERCHANT) {
			MERCHANT = mERCHANT;
		}
	}
	
	public static class TransaccionAprobada {
		private Header header;
		private Fullfillment fullfillment;
		private Order order;
		private DataMap dataMap;
		public TransaccionAprobada() {
			
		}
		public Header getHeader() {
			return header;
		}
		public void setHeader(Header header) {
			this.header = header;
		}
		public Fullfillment getFullfillment() {
			return fullfillment;
		}
		public void setFullfillment(Fullfillment fullfillment) {
			this.fullfillment = fullfillment;
		}
		public Order getOrder() {
			return order;
		}
		public void setOrder(Order order) {
			this.order = order;
		}
		public DataMap getDataMap() {
			return dataMap;
		}
		public void setDataMap(DataMap dataMap) {
			this.dataMap = dataMap;
		}
	}
	
	public static class TransaccionDenegada {
		private Integer errorCode;
		private String errorMessage;
		private Header header;
		private Data data;
		public TransaccionDenegada() {
			
		}
		public Integer getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(Integer errorCode) {
			this.errorCode = errorCode;
		}
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
		public Header getHeader() {
			return header;
		}
		public void setHeader(Header header) {
			this.header = header;
		}
		public Data getData() {
			return data;
		}
		public void setData(Data data) {
			this.data = data;
		}
	}
	
}

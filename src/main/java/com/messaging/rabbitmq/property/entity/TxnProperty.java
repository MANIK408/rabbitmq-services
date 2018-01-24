package com.messaging.rabbitmq.property.entity;

public class TxnProperty {
	int txnNumber;
	PropertyBulkEntity bulkBean;

	public int getTxnNumber() {
		return txnNumber;
	}

	public void setTxnNumber(int txnNumber) {
		this.txnNumber = txnNumber;
	}

	public PropertyBulkEntity getBulkBean() {
		return bulkBean;
	}

	public void setBulkBean(PropertyBulkEntity bulkBean) {
		this.bulkBean = bulkBean;
	}

}

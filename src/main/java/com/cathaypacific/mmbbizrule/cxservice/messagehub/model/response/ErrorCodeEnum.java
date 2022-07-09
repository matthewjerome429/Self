package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

/**
 * @author mingming.a.shen
 */

public enum ErrorCodeEnum {

	INVALID_REQUEST("E30000000", ErrorTypeEnum.BUSERROR, "Invalid request"),
	SYSTEM_ERROR("E30000001", ErrorTypeEnum.BUSERROR, "System error"),
	NO_JOBID("E30000002", ErrorTypeEnum.BUSERROR, "No jobId provided"),
	MONGO_DB_EXCEPRTION("E30000003", ErrorTypeEnum.BUSERROR, "Mongo DB exception"),
	BATCH_CAPABILITY_ERROR("E30000004", ErrorTypeEnum.BUSERROR, "Excess Batch Put Message limit"),
	MESSAGE_COLLECTION_LIMITATION_ERROR("E30000005", ErrorTypeEnum.BUSERROR, "Excess Message per collection limit"),
	COLLECTION_LIMITATION_ERROR("E30000006", ErrorTypeEnum.BUSERROR, "Exceed Collections Maximum Limit"),
	MESSAGE_LIMITATION_ERROR("E30000007", ErrorTypeEnum.BUSERROR, "Exceed Message Maximum Limit"),
	EVTID_LIMITATION_ERROR("E30000008", ErrorTypeEnum.BUSERROR, "Exceed EvtId Maximum Limit"),
	EVTTYPE_LIMITATION_ERROR("E30000009", ErrorTypeEnum.BUSERROR, "Exceed EvtType Maximum Limit");


	private ErrorCodeEnum(String code, ErrorTypeEnum type,String message) {
		this.code = code;
		this.type = type;
		this.message = message;
	}

	private String code;

	private ErrorTypeEnum type;

	private String message;

	public String getCode() {
		return code;
	}

	public ErrorTypeEnum getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}

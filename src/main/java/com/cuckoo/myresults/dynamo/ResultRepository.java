package com.cuckoo.myresults.dynamo;

import com.cuckoo.myresults.model.Result;

public interface ResultRepository {
	public Result getResult(String exam, String enrollmentId);
}

package com.cuckoo.myresults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cuckoo.myresults.dynamo.ResultRepository;
import com.cuckoo.myresults.model.Response;
import com.cuckoo.myresults.model.Result;

@RestController
public class MyResultController {
	private final ResultRepository resultRepository;

	@Autowired
	public MyResultController(ResultRepository resultRepository) {
		this.resultRepository = resultRepository;
	}

	@RequestMapping(value = "/getresult")
	@ResponseBody
	public Response<Result> getBarBySimplePathWithRequestParam(@RequestParam("enrollmentId") String enrollmentId, @RequestParam("exam") String exam) {
		Result result =  resultRepository.getResult(exam, enrollmentId);
		return new Response<Result>(result);
	}

}

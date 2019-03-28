import com.github.tomakehurst.wiremock.http.Body

import org.springframework.cloud.contract.spec.Contract

Contract.make { 
	description="Should execute python code snippet and return 2"
	request{
		method 'POST'
		url '/interpreter/execute'
		headers {
			header('sessionId': 'khalid123')
			header('Content-Type': 'application/json')
		  }
		body("""
		{
			"code":"%python print(1+1)"
		}
			""")
	}
	response{
		status 200
		body("""
		{
			"result":"2"
		}
			""")
 
	}
}
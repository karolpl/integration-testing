package generated.api;

import generated.invoker.ApiClient;
import generated.model.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountDetailsApi {
    private ApiClient apiClient;

    public AccountDetailsApi() {
        this(new ApiClient());
    }

    @Autowired
    public AccountDetailsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    private ResponseSpec getAccountDetailsRequestCreation(String accountName) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'accountName' is set
        if (accountName == null) {
            throw new WebClientResponseException("Missing the required parameter 'accountName' when calling getAccountDetails", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("accountName", accountName);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json;version=1", "application/problem+json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<AccountDetails> localVarReturnType = new ParameterizedTypeReference<AccountDetails>() {};
        return apiClient.invokeAPI("/account-details/{accountName}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    public Mono<AccountDetails> getAccountDetails(String accountName) throws WebClientResponseException {
        ParameterizedTypeReference<AccountDetails> localVarReturnType = new ParameterizedTypeReference<AccountDetails>() {};
        return getAccountDetailsRequestCreation(accountName).bodyToMono(localVarReturnType);
    }

    public Mono<ResponseEntity<AccountDetails>> getAccountDetailsWithHttpInfo(String accountName) throws WebClientResponseException {
        ParameterizedTypeReference<AccountDetails> localVarReturnType = new ParameterizedTypeReference<AccountDetails>() {};
        return getAccountDetailsRequestCreation(accountName).toEntity(localVarReturnType);
    }
}

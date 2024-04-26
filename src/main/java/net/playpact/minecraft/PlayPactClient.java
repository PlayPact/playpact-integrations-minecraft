package net.playpact.minecraft;

import org.openapitools.client.ApiCallback;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.api.ServerApi;
import org.openapitools.client.api.UserApi;
import org.openapitools.client.auth.ApiKeyAuth;
import org.openapitools.client.model.PlatformIdentifierEnumDto;
import org.openapitools.client.model.ServerChallengeDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Copyright 2024 (C) CUTE.DEV
 *
 * @author: Lukas Klepper
 * <p>
 * -----------------------------------------------------------------------------
 * Revision History
 * -----------------------------------------------------------------------------
 * VERSION     AUTHOR/      DESCRIPTION OF CHANGE
 * OLD/NEW     DATE
 * -----------------------------------------------------------------------------
 * NO RC   | Lukas Klepper | Initial Create.
 * | 11.02.2024    |
 * ---------|---------------|---------------------------------------------------
 */
public class PlayPactClient {
    private ApiClient defaultClient;
    private Logger logger;

    private final ApiCallback<Void> informationCallback = new ApiCallback<Void>() {
        @Override
        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
            logger.warning("Information couldn't be delivered to PlayPact");
            e.printStackTrace();
        }

        @Override
        public void onSuccess(Void result, int statusCode, Map<String, List<String>> responseHeaders) {}

        @Override
        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {}

        @Override
        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {}
    };

    public PlayPactClient(Logger logger){
        this.logger = logger;

        defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:5275");
        defaultClient.setConnectTimeout(1000 * 10);
        defaultClient.setReadTimeout(1000 * 10);
        defaultClient.setWriteTimeout(1000 * 10);

        // Configure API key authorization: Bearer
        ApiKeyAuth Bearer = (ApiKeyAuth) defaultClient.getAuthentication("Bearer");
        Bearer.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //Bearer.setApiKeyPrefix("Token");


    }

    public boolean checkPlayPactConnection() {
        ServerApi apiInstance = new ServerApi(defaultClient);

        try{
            apiInstance.getServers();
            return true;
        } catch (ApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void playerWasKicked(UUID uniqueId, String reason) {
        UserApi apiInstance = new UserApi(defaultClient);

        try {
            apiInstance.userKickedFromServerAsync(informationCallback);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void playerWasBanned(UUID uniqueId, String reason) {
        UserApi apiInstance = new UserApi(defaultClient);

        try {
            apiInstance.userBannedFromServerAsync(informationCallback);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void playerJoinedServer(UUID uniqueId) {
        UserApi apiInstance = new UserApi(defaultClient);

        try {
            apiInstance.userJoinedServerAsync(informationCallback);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void playerLeftServer(UUID uniqueId) {
        UserApi apiInstance = new UserApi(defaultClient);

        try {
            apiInstance.userLeftServerAsync(informationCallback);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerAllowedToJoin(UUID uniqueId, String serverIdentifier){
        UserApi apiInstance = new UserApi(defaultClient);

        ServerChallengeDto challenge = new ServerChallengeDto();
        challenge.playerPlatformIdentifier(uniqueId.toString());
        challenge.serverIdentifier(serverIdentifier);

        try {
            return apiInstance.userChallengesServerLogin(PlatformIdentifierEnumDto.MINECRAFT, challenge);
        } catch (ApiException e) {
            e.printStackTrace();
            return false;
        }
    }
}

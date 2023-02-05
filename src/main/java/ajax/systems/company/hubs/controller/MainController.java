package ajax.systems.company.hubs.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import ajax.systems.company.hubs.dto.group.Group;
import ajax.systems.company.hubs.dto.hub.HubCompanyBinding;
import ajax.systems.company.hubs.dto.hub.HubDetail;
import ajax.systems.company.hubs.dto.object.ObjectBriefInfo;
import ajax.systems.company.hubs.exception.Response4xxException;
import ajax.systems.company.hubs.exception.Response5xxException;
import ajax.systems.company.hubs.model.CompanyHub;
import ajax.systems.company.hubs.model.Credentials;
import ajax.systems.company.hubs.service.IAjaxGroupService;
import ajax.systems.company.hubs.service.IAjaxHubService;
import ajax.systems.company.hubs.service.IAjaxObjectService;
import ajax.systems.company.hubs.utils.Constants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Component
public class MainController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private Credentials credentials;
	private ViewController activeView;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	private IAjaxHubService hubService;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	private IAjaxObjectService objectService;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	private IAjaxGroupService groupService;
	
	
	protected void retrieveDataFromAjax() {
		Thread retrieveDataExecutor = new Thread(() -> {
			if(credentials != null) {
				try {
					ResponseEntity<HubCompanyBinding[]> response = hubService.listHubsPerCompany(credentials);
					if(response != null){
						List<CompanyHub> companyHubs = enrichHubDetails(response.getBody());
					}
				}catch(Response5xxException ex) {
					ex.printStackTrace();
					activeView.handleException(ex, "Error occured during retrieving data from Ajax Systems");
				}catch(Response4xxException ex) {
					ex.printStackTrace();
					activeView.handleException(ex, "Error occured during retrieving data from Ajax Systems");
				}catch(Exception ex) {
					ex.printStackTrace();
					activeView.handleException(ex, "Error occured during retrieving data from Ajax Systems");
				}
			}else {
				
			}
		});
		retrieveDataExecutor.setName(Constants.GET_DATA_THREAD_NAME);
		retrieveDataExecutor.setDaemon(true);
		retrieveDataExecutor.start();
	}
	
	
	private List<CompanyHub> enrichHubDetails(HubCompanyBinding[] hubs) {
		List<CompanyHub> result = new ArrayList<>();
		for(HubCompanyBinding hubBinding : hubs) {
			CompanyHub companyHub = new CompanyHub();
			companyHub.setHubId(hubBinding.getHubId());
			ExecutorService hubDetailsRequestExecutor = Executors.newSingleThreadExecutor();
			Callable<ResponseEntity<HubDetail>> callableHubDetailsRequest = () -> {
				ResponseEntity<HubDetail> hubDetailResponse = hubService.getHubDetails(credentials, hubBinding.getHubId());
				return hubDetailResponse;
			};
			ExecutorService hubObjectsRequestExecutor = Executors.newSingleThreadExecutor();
			Callable<ResponseEntity<ObjectBriefInfo[]>> callableHubObjectsRequest = () -> {
				ResponseEntity<ObjectBriefInfo[]> hubObjectResponse = objectService.getAvailableObjectsOfHub(credentials, hubBinding.getHubId());
				return hubObjectResponse;
			};
			ExecutorService hubGroupsRequestExecutor = Executors.newSingleThreadExecutor();
			Callable<ResponseEntity<Group[]>> callableHubGroupsRequest = () -> {
				ResponseEntity<Group[]> groupResponse = groupService.listGroupsPerHub(credentials, hubBinding.getHubId());
				return groupResponse;
			};
			Future<ResponseEntity<HubDetail>> hubDetailsResponseFuture = hubDetailsRequestExecutor.submit(callableHubDetailsRequest);
			Future<ResponseEntity<ObjectBriefInfo[]>> hubObjectsResponseFuture = hubObjectsRequestExecutor.submit(callableHubObjectsRequest);
			Future<ResponseEntity<Group[]>> hubGroupsResponseFuture = hubGroupsRequestExecutor.submit(callableHubGroupsRequest);
			
			try {
				ResponseEntity<HubDetail> hubDetailsResponse = hubDetailsResponseFuture.get();
				ResponseEntity<ObjectBriefInfo[]> hubObjectsResponse = hubObjectsResponseFuture.get();
				ResponseEntity<Group[]> hubGroupsResponse = hubGroupsResponseFuture.get();
				if(hubDetailsResponse != null) {
					companyHub.setHubDetails(hubDetailsResponse.getBody());
				}
				if(hubGroupsResponse != null) {
					companyHub.setGroups(hubGroupsResponse.getBody());
				}
				if(hubObjectsResponse != null) {
					companyHub.setObjectInfoes(hubObjectsResponse.getBody());
				}
				
			}catch(Exception ex) {
				logger.warn("Error occured during parallel service call to Ajax Systems",ex);
				ex.printStackTrace();
			}
			result.add(companyHub);
		}
		return result;
	}
}

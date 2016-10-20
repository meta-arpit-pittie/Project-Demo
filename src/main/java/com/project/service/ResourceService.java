package com.project.service;

/**
 * Class To implement the service layer for the Users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

import java.util.ArrayList;
import java.util.List;








import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.project.model.BookingsModel;
import com.project.model.BookingsVO;
import com.project.model.ResourcesModel;
import com.project.model.ResourcesVO;
import com.project.model.UsersModel;
import com.project.model.UsersVO;

@Service("resourceService")
@Transactional
public class ResourceService {

	@Autowired
	private ResourceDAO resourceDAO;

	// To get the beans
	@Autowired
	private ApplicationContext context;

	/**
	 * To get the list of all available resources from ResourcesDao class
	 * 
	 * @return - List of all the available resources
	 */
	
	public List<ResourcesVO> allResourceList() {

		List<ResourcesModel> resourcesModel = new ArrayList<ResourcesModel>();

		resourcesModel = resourceDAO.allResourceList();
		List<ResourcesVO> resourcesVO = new ArrayList<ResourcesVO>(
				resourcesModel.size());
		ResourcesVO resourceVO;
		System.out.println(" model data" + resourcesModel.size());
		for (int i = 0; i < resourcesModel.size(); i++) {
			resourceVO = context.getBean(ResourcesVO.class);
			BeanUtils.copyProperties(resourcesModel.get(i), resourceVO);
			resourcesVO.add(resourceVO);
		}

		System.out.println("vo data" + resourcesVO);
		return resourcesVO;
	}

	/**
	 * Following function helps in creating a new resource. 
	 * @param resourcesVO contains the information of the new resource.
	 * @return true/false whether resource created successfully or not.
	 */
	public boolean createResource(ResourcesVO resourcesVO) {
		ResourcesModel resourceModel = context.getBean(ResourcesModel.class);
		BeanUtils.copyProperties(resourcesVO, resourceModel);
		return resourceDAO.createResource(resourceModel);
	}


	/**
	 * Following function helps in deleting an existing resource. 
	 * @param resourcesVO contains the information of resource to be deleted.
	 * @return true/false whether resource deleted successfully or not.
	 */
	/*public boolean deleteResource(ResourcesVO resourcesVO) {
		ResourcesModel resourceModel = context.getBean(ResourcesModel.class);
		BeanUtils.copyProperties(resourcesVO, resourceModel);
		return resourceDAO.deleteResource(resourceModel);
	}*/


	/**
	 * Following function helps in updating the existing resource. 
	 * @param resourcesVO contains the information of the resource to be updated.
	 * @return true/false whether resource updated successfully or not.
	 */
	public boolean editResource(ResourcesVO resourcesVO) {
		ResourcesModel resourceModel = context.getBean(ResourcesModel.class);
		resourceModel = convertResourcesVOToResourcesModel(resourcesVO);
		List<UsersModel> newResAdmins = resourceModel.getResourceAdmins();
		
		resourceDAO.deleteResourceAdmin(resourceModel);
		
		for (UsersModel usersModel : newResAdmins) {
			resourceDAO.addResourceAdmin(resourceModel, usersModel);
		}
		return resourceDAO.editResource(resourceModel);
	}

	private ResourcesModel convertResourcesVOToResourcesModel(
			ResourcesVO resourcesVO) {
		
		ResourcesModel resourceModel = context.getBean(ResourcesModel.class);
		UsersModel usersModel;
		List<UsersModel> usersModelList = new ArrayList<UsersModel>();
		
		resourceModel.setResourceId(resourcesVO.getResourceId());
		resourceModel.setResourceName(resourcesVO.getResourceName());
		resourceModel.setType(resourcesVO.getType());
		resourceModel.setCapacity(resourcesVO.getCapacity());
		
		for ( UsersVO  userVO : resourcesVO.getResourceAdmins() ) {
			
			usersModel = context.getBean(UsersModel.class);
			
			BeanUtils.copyProperties(userVO, usersModel);
			
			usersModelList.add(usersModel);
			
		}
		
		
		
		resourceModel.setResourceAdmins(usersModelList);
		
		
		BookingsModel bookingsModel;
		List<BookingsModel> bookingsModelList = new ArrayList<BookingsModel>();
		
		for ( BookingsVO  bookingsVO : resourcesVO.getBookedList() ) {
			
			bookingsModel = context.getBean(BookingsModel.class);
			
			BeanUtils.copyProperties(bookingsVO, bookingsModel);
			
			bookingsModelList.add(bookingsModel);
			
		}
		
		resourceModel.setBookedList(bookingsModelList);
		
		return resourceModel;
	}

	/*public boolean createResourceAdmin(ResourcesVO resourcesVO, UsersVO usersVO) {
		
		ResourcesModel resourceModel = context.getBean(ResourcesModel.class);
		UsersModel usersModel = context.getBean(UsersModel.class);
		BeanUtils.copyProperties(resourcesVO, resourceModel);
		BeanUtils.copyProperties(usersVO, usersModel);
		return resourceDAO.createResourceAdmin(resourceModel, usersModel);
	}*/

}
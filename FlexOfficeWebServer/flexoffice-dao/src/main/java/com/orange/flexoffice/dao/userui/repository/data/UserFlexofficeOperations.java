package com.orange.flexoffice.dao.userui.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.userui.model.data.UserFlexoffice;

public interface UserFlexofficeOperations {
	
	List<UserFlexoffice> findByUserId(Long userId);
	
	List<UserFlexoffice> findByUserEmail(String userEmail);
	
}

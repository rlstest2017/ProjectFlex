package com.orange.flexoffice.business.gatewayapi.service.object;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.dao.gatewayapi.model.data.Log;
import com.orange.flexoffice.dao.gatewayapi.model.object.Item;

public interface ItemManager {

	Item find(String itemId);

	List<Log> getItemLogs(String itemId);

	List<Characteristic> getItemCharacteristics(String itemId);

}
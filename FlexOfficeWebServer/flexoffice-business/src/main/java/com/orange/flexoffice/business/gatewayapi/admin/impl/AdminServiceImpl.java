package com.orange.flexoffice.business.gatewayapi.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.admin.AdminService;
import com.orange.flexoffice.business.gatewayapi.admin.SimilarityMatrixInfo;
import com.orange.flexoffice.business.gatewayapi.admin.SourceInfo;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrix;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixManager;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixType;
import com.orange.flexoffice.business.gatewayapi.source.SourceType;

@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService {

	@Autowired
	private SimilarityMatrixManager similarityMatrixManager;

	@Override
	public void generateSimilarityMatrix(SimilarityMatrixType type, int knn,
			int speedFactor) {
		SimilarityMatrix matrix = similarityMatrixManager.findByType(type);
		matrix.generateKNNTable(false);
	}

	@Override
	public SourceInfo getSourceStatistics(SourceType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimilarityMatrixInfo getSimilarityStatistics(
			SimilarityMatrixType type) {
		// TODO Auto-generated method stub
		return null;
	}

}

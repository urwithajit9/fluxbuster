/*
* Copyright (C) 2012 Chris Neasbitt
* Author: Chris Neasbitt
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package edu.uga.cs.fluxbuster.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jolbox.bonecp.BoneCP;

import edu.uga.cs.fluxbuster.analytics.ClusterSimilarity;
import edu.uga.cs.fluxbuster.classification.ClusterClass;
import edu.uga.cs.fluxbuster.clustering.DomainCluster;
import edu.uga.cs.fluxbuster.clustering.StoredDomainCluster;

/**
 * The base class for any database interface implementation.
 * 
 * @author Chris Neasbitt
 */
public abstract class DBInterface {
	
	protected BoneCP connectionPool = null;
	
	/**
	 * Instantiates a new database interface.
	 *
	 * @param connectionPool the BoneCP connection pool from
	 * 		which to generate connections.
	 */
	public DBInterface(BoneCP connectionPool) {
		this.connectionPool = connectionPool;
	}
	
	/**
	 * Creates a connection to the database.
	 *
	 * @return the connection
	 * @throws SQLException the SQL exception if a connection can not be made.
	 */
	protected final Connection getConnection() throws SQLException{
		return this.connectionPool.getConnection();
	}
	
	
	/**
	 * Gets the connect string used to connect to the database.
	 *
	 * @return the connect string
	 */
	public final String getConnectString(){
		return this.connectionPool.getConfig().getJdbcUrl();
	}

	/**
	 * Store domain clusters in the database.
	 *
	 * @param clusters the clusters to store
	 * @param sensorname the sensorname
	 * @param logdate the date for the run
	 */
	public abstract void storeClusters(List<DomainCluster> clusters,
			String sensorname, Date logdate);
	
	
	/**
	 * Return the list of cluster ids for a run.
	 *
	 * @param logdate the date of the run
	 * @return the list of cluster ids
	 */
	public abstract List<Integer> getClusterIds(Date logdate);
	
	
	/**
	 * Return the list of cluster ids for a run for clusters that have the 
	 * supplied cluster classification.
	 * 
	 * @param logdate the date of the run
	 * @param cls the cluster class
	 * @return the list of cluster ids
	 */
	public abstract List<Integer> getClusterIds(Date logdate, ClusterClass cls);
	
	
	/**
	 * Return the list of cluster ids for a run for clusters that have at minimum
	 * the network cardinality supplied.
	 * 
	 * @param logdate the date of the run
	 * @param minCardinality the minimum network cardinality
	 * @return the list of cluster ids
	 */
	public abstract List<Integer> getClusterIds(Date logdate, int minCardinality);
	
	/**
	 * Return the list of clusters for a run.
	 * 
	 * @param logdate the date of the run
	 * @return the list of clusters
	 */
	public abstract List<StoredDomainCluster> getClusters(Date logdate);
	
	
	/**
	 * Return the list of clusters for a run having the supplied cluster classification.
	 * 
	 * @param logdate the date of the run
	 * @param cls the cluster class
	 * @return the list of clusters
	 */
	public abstract List<StoredDomainCluster> getClusters(Date logdate, ClusterClass cls);
	
	/**
	 * Return the list of clusters for a run having at minimum the supplied 
	 * network cardinality.
	 * 
	 * @param logdate the date of the run
	 * @param minCardinality the minimum network cardinality
	 * @return the list of clusters
	 */
	public abstract List<StoredDomainCluster> getClusters(Date logdate, int minCardinality);
	
	
	/**
	 * Return the cluster with the supplied id from a run.
	 * 
	 * @param logdate the date of the run
	 * @param clusterId the id of the cluster to retrieve
	 * @return the stored cluster or null in case of an error
	 */
	public abstract StoredDomainCluster getCluster(Date logdate, int clusterId);
	
	
	/**
	 * Store the ip cluster similarities in the database.
	 *
	 * @param sims the similarities to store
	 */
	public abstract void storeIpClusterSimilarities(List<ClusterSimilarity> sims);

	
	/**
	 * Store the domainname cluster similarities in the database.
	 *
	 * @param sims the similarities to store
	 */
	public abstract void storeDomainnameClusterSimilarities(List<ClusterSimilarity> sims);
	
	/**
	 * Stores the results of cluster classification in the database. 
	 * 
	 * @param logdate the date of the run
	 * @param clusterClasses the cluster classes
	 * @param validated if the classification has been manually validated
	 */	
	public abstract void storeClusterClasses(Date logdate, 
			Map<ClusterClass, List<StoredDomainCluster>> clusterClasses, 
			boolean validated);
	
	/**
	 * Execute query with result.
	 *
	 * @param query the query to execute
	 * @return the result set or null if there is an error executing
	 * 		the query.
	 */
	public abstract ResultSet executeQueryWithResult(String query);
	
	/**
	 * Execute query with no result.
	 *
	 * @param query the query to execute
	 */
	public abstract void executeQueryNoResult(String query);
	
	
	/**
	 * Creates all of the run tables and indexes in the database.
	 * 
	 * @param logdate the data of the run
	 */
	public abstract void initAllTables(Date logdate);
	
	/**
	 * Creates the run tables and indexes for clustering and 
	 * longitudinal feature calculation in the database.
	 * 
	 * @param logdate the data of the run
	 */
	public abstract void initClusterTables(Date logdate);
	
	
	/**
	 * Creates all of the run tables and indexes for calculating
	 * cluster similarity in the database.
	 * 
	 * @param logdate the data of the run
	 */
	public abstract void initSimilarityTables(Date logdate);
	
	
	/**
	 * Creates all of the run tables and indexes for cluster
	 * classification in the database.
	 * 
	 * @param logdate the data of the run
	 */
	public abstract void initClassificationTables(Date logdate);

}

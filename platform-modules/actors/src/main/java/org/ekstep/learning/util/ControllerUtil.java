package org.ekstep.learning.util;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ilimi.common.dto.Request;
import com.ilimi.common.dto.Response;
import com.ilimi.graph.dac.enums.GraphDACParams;
import com.ilimi.graph.dac.model.Node;
import com.ilimi.graph.engine.router.GraphEngineManagers;
import com.ilimi.graph.model.node.DefinitionDTO;

// TODO: Auto-generated Javadoc
/**
 * The Class ControllerUtil, provides controller utility functionality for all
 * learning actors.
 *
 * @author karthik
 */
public class ControllerUtil extends BaseLearningManager {

	/** The logger. */
	private static Logger LOGGER = LogManager.getLogger(ControllerUtil.class.getName());

	/**
	 * Gets the node.
	 *
	 * @param taxonomyId
	 *            the taxonomy id
	 * @param contentId
	 *            the content id
	 * @return the node
	 */
	public Node getNode(String taxonomyId, String contentId) {
		Request request = getRequest(taxonomyId, GraphEngineManagers.SEARCH_MANAGER, "getDataNode",
				GraphDACParams.node_id.name(), contentId);
		request.put(GraphDACParams.get_tags.name(), true);
		Response getNodeRes = getResponse(request, LOGGER);
		Response response = copyResponse(getNodeRes);
		if (checkError(response)) {
			return null;
		}
		Node node = (Node) getNodeRes.get(GraphDACParams.node.name());
		return node;
	}

	/**
	 * Update node.
	 *
	 * @param node
	 *            the node
	 * @return the response
	 */
	public Response updateNode(Node node) {
		Request updateReq = getRequest(node.getGraphId(), GraphEngineManagers.NODE_MANAGER, "updateDataNode");
		updateReq.put(GraphDACParams.node.name(), node);
		updateReq.put(GraphDACParams.node_id.name(), node.getIdentifier());
		Response updateRes = getResponse(updateReq, LOGGER);
		return updateRes;
	}

	/**
	 * Gets the definition.
	 *
	 * @param taxonomyId
	 *            the taxonomy id
	 * @param objectType
	 *            the object type
	 * @return the definition
	 */
	public DefinitionDTO getDefinition(String taxonomyId, String objectType) {
		Request request = getRequest(taxonomyId, GraphEngineManagers.SEARCH_MANAGER, "getNodeDefinition",
				GraphDACParams.object_type.name(), objectType);
		Response response = getResponse(request, LOGGER);
		if (!checkError(response)) {
			DefinitionDTO definition = (DefinitionDTO) response.get(GraphDACParams.definition_node.name());
			return definition;
		}
		return null;
	}
	
	public Response addOutRelations(String taxonomyId, String startNodeId, List<String> endNodeIds, String relationType){
		Request request = getRequest(taxonomyId, GraphEngineManagers.GRAPH_MANAGER, "addOutRelations",
				GraphDACParams.start_node_id.name(), startNodeId);
		request.put(GraphDACParams.relation_type.name(), relationType);
		request.put(GraphDACParams.end_node_id.name(), endNodeIds);
		Response response = getResponse(request, LOGGER);
		if (!checkError(response)){
			return response;
		}
		return null;
	}
	
	public Response getCollectionMembers(String taxonomyId, String collectionId, String collectionType){
		Request request = getRequest(taxonomyId, GraphEngineManagers.COLLECTION_MANAGER, "getCollectionMembers",
				GraphDACParams.collection_id.name(), collectionId);
		request.put(GraphDACParams.collection_type.name(), collectionType);
		Response response = getResponse(request, LOGGER);
		if (!checkError(response)){
			return response;
		}
		return null;
	}
	
	public Response getDataNodes(String taxonomyId, List<String> existingConcepts){
		Request request = getRequest(taxonomyId, GraphEngineManagers.SEARCH_MANAGER, "getDataNodes",
				GraphDACParams.node_ids.name(), existingConcepts);
		Response response = getResponse(request, LOGGER);
		if (!checkError(response)){
			return response;
		}
		return null;
	}
	
	/**
	 * Sets the context.
	 *
	 * @param request
	 *            the request
	 * @param graphId
	 *            the graph id
	 * @param manager
	 *            the manager
	 * @param operation
	 *            the operation
	 * @return the request
	 */
	public Request setLanguageContext(Request request,String languageId, String graphId, String manager, String operation) {
		request.getContext().put(languageId, graphId);
		request.setManagerName(manager);
		request.setOperation(operation);
		return request;
	}
	
	/**
	 * Gets the request from the Language request router.
	 *
	 * @param graphId
	 *            the graph id
	 * @param manager
	 *            the manager
	 * @param operation
	 *            the operation
	 * @return the language request
	 */
	public Request getLanguageRequest(String graphId, String manager, String languageId, String operation) {
		Request request = new Request();
		return setLanguageContext(request, graphId, manager,languageId, operation);
	}

	public Response getComplexityMeasures(String graphId, String languageId, String text){
		Request request = getLanguageRequest(graphId, "LEXILE_MEASURES_ACTOR", languageId, "computeTextComplexity");
		Response response = getResponse(request, LOGGER);
		if (!checkError(response)){
			return response;
		}
		return null;
	}
}

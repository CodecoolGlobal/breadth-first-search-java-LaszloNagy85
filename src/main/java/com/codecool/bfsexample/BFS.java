package com.codecool.bfsexample;

import com.codecool.bfsexample.model.UserNode;

import java.util.*;

public class BFS {
    private List<UserNode> users = new ArrayList<>();

    private Queue<List<UserNode>> queue;
    private Set<UserNode> visitedNodes;
    private GraphPlotter graphPlotter;

    //entry node to start work with
    private UserNode startNode;

    //the actual node in use
    private UserNode currentNode;

    //a collection to hold the path through which a node has been reached
    private List<UserNode> pathToCurrentNode;

    private List<List<UserNode>> ways;

    public BFS(List<UserNode> users, GraphPlotter graphPlotter) {
        this.users = users;
        this.graphPlotter = graphPlotter;
    }

    public boolean findShortestRouteBetweenUsersById(int fromUserId, int targetUserId) {

        if (!isValidInput(fromUserId, targetUserId)) {
            return false;
        }

        initializeFields(fromUserId);

        while (!queue.isEmpty()) {

            pathToCurrentNode = queue.poll();
            currentNode = pathToCurrentNode.get(pathToCurrentNode.size() - 1);

            for (UserNode nextNode : currentNode.getFriends()) {
                if (!isVisited(nextNode)) {
                    //create a new collection representing the path to nextNode
                    List<UserNode> pathToNextNode = new ArrayList<>(pathToCurrentNode);
                    pathToNextNode.add(nextNode);

                    if (targetFound(nextNode, targetUserId)) {
                        ways.add(pathToNextNode);
                        displayShortestRouteResult(startNode, ways, users.get(targetUserId));
                        return true;
                    }
                    queue.add(pathToNextNode);
                }
            }
        }
        return false;
    }


    public void friendsOfFriends(int userId, int distance) {

        initializeFields(userId);

        Set<UserNode> friendsInSetDistance = new HashSet<>();

        while (!queue.isEmpty()) {

            pathToCurrentNode = queue.poll();
            currentNode = pathToCurrentNode.get(pathToCurrentNode.size() - 1);

            for (UserNode nextNode : currentNode.getFriends()) {
                if (!isVisited(nextNode)) {
                    //create a new collection representing the path to nextNode
                    List<UserNode> pathToNextNode = new ArrayList<>(pathToCurrentNode);
                    pathToNextNode.add(nextNode);
                    if (pathToNextNode.size() > distance + 1) {
                        displayFriendsOfFriendsResult(friendsInSetDistance, distance);
                        return;
                    }
                    friendsInSetDistance.addAll(pathToNextNode);
                    queue.add(pathToNextNode);
                }
            }
        }
    }


    public boolean findAllSameLengthShortestRouteBetweenUsers(int fromUserId, int targetUserId) {

        if (!isValidInput(fromUserId, targetUserId)) {
            return false;
        }

        initializeFields(fromUserId);

        while (!queue.isEmpty()) {

            pathToCurrentNode = queue.poll();
            currentNode = pathToCurrentNode.get(pathToCurrentNode.size() - 1);

            if (targetFound(currentNode, targetUserId)) {
                ways.add(pathToCurrentNode);
            }

            //Missing logic to handle restart on different route

            for (UserNode nextNode : currentNode.getFriends()) {
                if (!isVisited(nextNode)) {
                    //create a new collection representing the path to nextNode
                    List<UserNode> pathToNextNode = new ArrayList<>(pathToCurrentNode);
                    pathToNextNode.add(nextNode);
                    queue.add(pathToNextNode);
                }
            }
        }
        if (!ways.isEmpty()) {
            displayShortestRouteResult(startNode, ways, users.get(targetUserId));
            return true;
        }

        return false;
    }


    /**
     * ****Helper methods*****
     * <p>
     * <p>
     * Clear/reinitialize fields for separate methods.
     */
    private void initializeFields(int fromUserId) {
        startNode = users.get(fromUserId);
        queue = new LinkedList<>();
        visitedNodes = new HashSet<>();
        pathToCurrentNode = new ArrayList<>();
        currentNode = null;
        pathToCurrentNode.add(startNode);
        queue.add(pathToCurrentNode);
        ways = new ArrayList<>();
    }

    private boolean isValidInput(int fromUserId, int targetUserId) {
        return !(fromUserId == targetUserId || fromUserId > 120 || fromUserId < 0 || targetUserId < 0 || targetUserId > 120);
    }


    private boolean targetFound(UserNode currentNode, int targetNodeId) {
        return currentNode.getId() == targetNodeId;
    }

    private boolean isVisited(UserNode node) {
        if (visitedNodes.contains(node)) {
            return true;
        }
        visitedNodes.add(node);
        return false;
    }

    private void displayShortestRouteResult(UserNode startNode, List<List<UserNode>> ways, UserNode targetNode) {
        System.out.println("\nShortest route length from " + startNode
                + " to " + targetNode + " is " + (ways.get(0).size() - 1) + ".");
        System.out.println(ways);
        graphPlotter.highlightNodes(Set.of(startNode), targetNode);
        graphPlotter.highlightRoute(ways);
    }

    private void displayFriendsOfFriendsResult(Set<UserNode> friendsInSetDistance, int distance) {
        graphPlotter.highlightNodes(friendsInSetDistance, startNode);
        friendsInSetDistance.remove(startNode);
        System.out.println("\nThere are " + friendsInSetDistance.size() + " friends from " + startNode +
                " in the distance of: " + distance + "\n" + friendsInSetDistance);
    }
}

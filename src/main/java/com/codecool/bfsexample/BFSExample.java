package com.codecool.bfsexample;

import com.codecool.bfsexample.model.UserNode;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BFSExample {

    private static List<UserNode> users;
    private static GraphPlotter graphPlotter;

    private static void populateDB() {

        RandomDataGenerator generator = new RandomDataGenerator();
        users = generator.generate();

        graphPlotter = new GraphPlotter(users);

        System.out.println("Done!");
    }

    public static void main(String[] args) {
        populateDB();
        BFS bfs = new BFS(users, graphPlotter);
        bfs.findShortestRouteBetweenUsersById(4, 6);
//        bfs.findAllSameLengthShortestRouteBetweenUsers(1, 2);
        bfs.friendsOfFriends(1, 2);

    }
}

package com.wildsmith.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.wildsmith.constants.CSSLocationConstants;
import com.wildsmith.objects.Player;
import com.wildsmith.position.PositionFactory;

public class MainThread {

    public static void main(String[] args) {
        System.out.println("--------------------------------------");
        System.out.println("--------------START-------------------");
        System.out.println("--------------------------------------");
        System.out.println("                                      ");
        System.out.println("                                      ");
        System.out.println("                                      ");

        generateDraftOrder();

        List<Player> rankedPlayers = new ArrayList<>();
        for (PositionFactory positionFactory : CSSLocationConstants.POSITION_FACTORIES) {
            rankedPlayers.addAll(positionFactory.build());
        }

        addADP(rankedPlayers);

        addDepthChart(rankedPlayers);

        handleCommands(rankedPlayers);

        System.out.println("                                      ");
        System.out.println("                                      ");
        System.out.println("                                      ");
        System.out.println("--------------------------------------");
        System.out.println("--------------Done--------------------");
        System.out.println("--------------------------------------");
    }

    /**
     * Note: The numbers do not correlate with draft order; simply with the id that could be assigned some
     * random draft position.
     * 
     * Number 1 == Johnnie Walkers
     * 
     * Number 2 == Zoidberg
     * 
     * Number 3 == Flaky Crust
     * 
     * Number 4 == The Derps
     * 
     * Number 5 == Sallad Rats
     * 
     * Number 6 == Linda's Legit Team
     * 
     * Number 7 == Sharknados
     * 
     * Number 8 == Flying Sex Snakes
     * 
     * Number 9 == Concussed,
     * 
     * Number 10 == Bear F!#%ers
     * 
     * Number 11 == Panda Swag
     * 
     * Number 12 == Adam's Team
     */
    private static void generateDraftOrder() {
        int size = 10;

        List<Integer> list = new ArrayList<>(size);
        for (int i = 1; i <= size; i++) {
            list.add(i);
        }

        Random rand = new Random();
        while (list.size() > 0) {
            int index = rand.nextInt(list.size());
            System.out.println("Selected: " + list.remove(index));
        }
    }

    private static void addADP(List<Player> rankedPlayers) {
        Document doc = getFantasyProsADP();
        if (doc == null || rankedPlayers == null || rankedPlayers.isEmpty()) {
            return;
        }

        System.out.println("                                      ");
        System.out.println("              Adding ADP              ");
        System.out.println("                                      ");

        Elements adpTableRows = selectTableRows(doc, CSSLocationConstants.FANTASY_PROS_ADP_TABLE_ROWS);
        if (adpTableRows == null) {
            return;
        }

        for (Element tableRow : adpTableRows) {
            if (tableRow == null) {
                continue;
            }

            int adpPosition = -1;
            boolean hasSetADP = false;
            for (Node childNode : tableRow.childNodes()) {
                if (hasSetADP) {
                    break;
                }

                if (childNode instanceof Element && "td".equals(((Element) childNode).tag().getName())) {
                    for (Node node : childNode.childNodes()) {
                        if (hasSetADP) {
                            break;
                        }

                        if (node instanceof TextNode) {
                            final String adpPositionString = ((TextNode) node).text();
                            if (" ".equals(adpPositionString)) {
                                break;
                            }

                            adpPosition = Integer.valueOf(adpPositionString);
                        } else if (node instanceof Element && "a".equals(((Element) node).tag().getName())) {
                            for (Node nameNode : node.childNodes()) {
                                if (nameNode instanceof TextNode) {
                                    final String fullName = ((TextNode) nameNode).text();
                                    if (fullName == null || "".equals(fullName)) {
                                        continue;
                                    }

                                    for (Player player : rankedPlayers) {
                                        if (fullName.contains(player.getFullName())) {
                                            player.setADP(adpPosition);
                                            hasSetADP = true;
                                            break;
                                        }
                                    }

                                    if (!hasSetADP) {
                                        hasSetADP = true;
                                        break;
                                    }
                                }

                                break;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("                                      ");
        System.out.println("           Done Adding ADP            ");
        System.out.println("                                      ");
    }

    private static Document getFantasyProsADP() {
        try {
            return Jsoup.connect(CSSLocationConstants.FANTASY_PROS_ADP_URI).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void addDepthChart(List<Player> rankedPlayers) {
        Document doc = getTheHuddleDepthCharts();
        if (doc == null || rankedPlayers == null || rankedPlayers.isEmpty()) {
            return;
        }

        System.out.println("                                      ");
        System.out.println("          Adding Depth Chart          ");
        System.out.println("                                      ");

        System.out.println("                                      ");
        System.out.println("       Done Adding Depth Chart        ");
        System.out.println("                                      ");
    }

    private static Document getTheHuddleDepthCharts() {
        try {
            return Jsoup.connect(CSSLocationConstants.THE_HUDDLE_DEPTH_CHART_URI).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ------------Command Options----------------
    // rm <Player Name>
    // print
    // show <Position>
    // exit
    // --------------------------------------------
    private static void handleCommands(List<Player> rankedPlayers) {
        System.out.println("                                      ");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Specify Command: ");
        final String command = scanner.nextLine();
        final String[] splitCommand = command.split(" ");
        switch (splitCommand[0]) {
            case "rm":
                System.out.println("Removing Player...");
                try {
                    removePlayer(splitCommand, rankedPlayers);
                } catch (Exception e) {
                    System.out.println("Issue occurred while removing player.");
                }
                handleCommands(rankedPlayers);
                break;
            case "print":
                try {
                    printPosition(splitCommand, rankedPlayers);
                } catch (Exception e) {
                    System.out.println("Issue occurred while printing csv.");
                }
                handleCommands(rankedPlayers);
                break;
            case "show":
                System.out.println("Showing Position...");
                try {
                    showPosition(splitCommand, rankedPlayers);
                } catch (Exception e) {
                    System.out.println("Issue occurred while showing position.");
                }
                handleCommands(rankedPlayers);
                break;
            case "exit":
                scanner.close();
                break;
            default:
                System.out.println("Invalid Command...");
                handleCommands(rankedPlayers);
                break;
        }
    }

    private static void removePlayer(String[] playerName, List<Player> rankedPlayers) {
        final String firstName = playerName[1];
        for (Player player : rankedPlayers) {
            if (firstName.equals(player.getFirstName())) {
                if (playerName.length <= 1 && player.getLastName() == null) {
                    rankedPlayers.remove(player);
                    break;
                } else {
                    final String lastName = playerName[2];
                    if (lastName.equals(player.getLastName())) {
                        rankedPlayers.remove(player);
                        break;
                    }
                }
            }
        }
    }

    private static void generateCsvFile(String sFileName, List<Player> rankedPlayers) {
        try {
            FileWriter writer = new FileWriter(sFileName);

            writer.append("Position");
            writer.append(',');
            writer.append("Name");
            writer.append(',');
            writer.append("Avg Ranking");
            writer.append(',');
            writer.append("Best Rank");
            writer.append(',');
            writer.append("Worst Rank");
            writer.append(',');
            writer.append("ADP");
            writer.append(',');
            writer.append("Team");
            writer.append(',');
            writer.append("Bye Week");
            writer.append('\n');

            for (Player player : rankedPlayers) {
                writer.append(player.getPosition().getName());
                writer.append(',');
                writer.append(player.getFullName());
                writer.append(',');
                writer.append("" + player.getAverageRanking());
                writer.append(',');
                writer.append("" + player.getBestRanking());
                writer.append(',');
                writer.append("" + player.getWorstRanking());
                writer.append(',');
                writer.append("" + player.getADP());
                writer.append(',');
                writer.append("" + player.getTeam());
                writer.append(',');
                writer.append("" + player.getByeWeek());
                writer.append('\n');
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showPosition(String[] positionCommand, List<Player> rankedPlayers) {
        String position = positionCommand[1];
        if (positionCommand.length > 2) {
            position += " " + positionCommand[2];
        }
        for (Player player : rankedPlayers) {
            if (position.equalsIgnoreCase(player.getPosition().getName())) {
                System.out.println(player.toString());
                System.out.println("                                      ");
            }
        }
    }

    private static void printPosition(String[] positionCommand, List<Player> rankedPlayers) {
        if (positionCommand.length <= 1) {
            generateCsvFile("c:\\Users\\Thomas\\Downloads\\FantasyRankings2015.csv", rankedPlayers);
        } else {
            String position = positionCommand[1];
            if (positionCommand.length > 2) {
                position += " " + positionCommand[2];
            }
            List<Player> playersForPosition = new ArrayList<>();
            for (Player player : rankedPlayers) {
                if (position.equalsIgnoreCase(player.getPosition().getName())) {
                    playersForPosition.add(player);
                }
            }
            generateCsvFile("c:\\Users\\Thomas\\Downloads\\FantasyRankings2015" + position + ".csv", playersForPosition);
        }
    }
}

package com.wildsmith.position;

import com.wildsmith.constants.Position;
import com.wildsmith.objects.Player;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PositionFactory {

    List<Player> build();

    static class PositionFactoryUtils {

        protected static Document getFantasyFootballRankings(String uri) {
            try {
                return Jsoup.connect(uri).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected static List<Player> build(Document doc, List<String> tableRows) {
            Map<Integer, List<Player>> players = new HashMap<Integer, List<Player>>(tableRows.size());
            for (int index = 0; index < tableRows.size(); index++) {
                Elements jeTableRows = selectTableRows(doc, tableRows.get(index));
                players.put(index, buildPlayerList(jeTableRows));
            }

            return rankPlayers(players);
        }

        protected static void printPlayers(List<Player> players) {
            for (Player player : players) {
                System.out.print(player.toString());
                System.out.print("\n\n");
            }
        }

        protected static Elements selectTableRows(Document doc, final String cssLocation) {
            return doc.select(cssLocation);
        }

        protected static List<Player> buildPlayerList(Elements jeQuaterbackTableRows) {
            Position position = null;
            List<Player> players = new ArrayList<Player>();
            for (Element tableRow : jeQuaterbackTableRows) {
                if (tableRow == null) {
                    continue;
                }

                if (tableRow.hasAttr("class") && tableRow.attr("class").equals("title")) {
                    for (Node childNode : tableRow.childNodes()) {
                        if (childNode instanceof Element) {
                            if ("td".equals(((Element) childNode).tag().getName())) {
                                for (Node node : childNode.childNodes()) {
                                    if (node instanceof TextNode) {
                                        position = Position.fromString(((TextNode) node).text());
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Player player = new Player();
                    player.setPosition(position);
                    int tableColumnIndex = 0;
                    for (Node childNode : tableRow.childNodes()) {
                        if (childNode instanceof Element && "td".equals(((Element) childNode).tag().getName())) {
                            for (Node node : childNode.childNodes()) {
                                if (node instanceof TextNode) {
                                    if (tableColumnIndex == 0) {
                                        player.setAverageRanking(Float.valueOf(((TextNode) node).text()));
                                    } else {
                                        player.setExtras(((TextNode) node).text());
                                    }
                                } else if (node instanceof Element && "a".equals(((Element) node).tag().getName())) {
                                    for (Node nameNode : node.childNodes()) {
                                        if (nameNode instanceof TextNode) {
                                            player.setFullName(((TextNode) nameNode).text());
                                        }
                                    }
                                }
                            }
                        }
                        tableColumnIndex++;
                    }
                    players.add(player);
                }
            }

            return players;
        }

        protected static List<Player> rankPlayers(List<Player> jePlayers, List<Player> drPlayers) {
            List<Player> rankedPlayerList = new ArrayList<Player>(jePlayers.size());
            for (Player jePlayer : jePlayers) {
                for (Player drPlayer : drPlayers) {
                    if (jePlayer.getFullName().equalsIgnoreCase(drPlayer.getFullName())) {
                        Player player = new Player();
                        player.setPosition(jePlayer.getPosition());
                        player.setFullName(jePlayer.getFullName());
                        player.setTeam(jePlayer.getTeam());
                        player.setByeWeek(jePlayer.getByeWeek());

                        float jePlayerAverageRanking = jePlayer.getAverageRanking();
                        float drPlayerAverageRanking = drPlayer.getAverageRanking();
                        if (jePlayerAverageRanking >= drPlayerAverageRanking) {
                            player.setBestRanking(drPlayerAverageRanking);
                            player.setWorstRanking(jePlayerAverageRanking);
                            player.setAverageRanking(jePlayerAverageRanking - ((jePlayerAverageRanking - drPlayerAverageRanking) / 2f));
                        } else {
                            player.setBestRanking(jePlayerAverageRanking);
                            player.setWorstRanking(drPlayerAverageRanking);
                            player.setAverageRanking(drPlayerAverageRanking - ((drPlayerAverageRanking - jePlayerAverageRanking) / 2f));
                        }

                        rankedPlayerList.add(player);
                        break;
                    }
                }
            }

            Collections.sort(rankedPlayerList);

            return rankedPlayerList;
        }

        protected static void printBlock(String comment) {
            System.out.println("                                      ");
            System.out.println("        " + comment + "      ");
            System.out.println("                                      ");
        }
    }
}
package com.wildsmith.position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;

import com.wildsmith.constants.CSSLocationConstants;
import com.wildsmith.objects.Player;

public class KPosition implements PositionFactory {

    private static final String FANTASY_RANKING_K_URI = "http://www.cbssports.com/fantasy/football/rankings/standard/K/yearly/";

    private static final List<String> KICKER_TABLE_ROWS;

    static {
        List<String> tableRows = new ArrayList<>();
        tableRows.add(CSSLocationConstants.JAMEY_EISENBERG_TABLE_ROWS);
        tableRows.add(CSSLocationConstants.DAVE_RICHARD_TABLE_ROWS);
        tableRows.add(CSSLocationConstants.HEATH_CUMMINGS_TABLE_ROWS);
        KICKER_TABLE_ROWS = Collections.unmodifiableList(tableRows);
    }

    @Override
    public List<Player> build() {
        Document doc = PositionFactoryUtils.getFantasyFootballRankings(FANTASY_RANKING_K_URI);
        if (doc == null) {
            return null;
        }

        System.out.println("                                      ");
        System.out.println("        Building Kicker Rankings      ");
        System.out.println("                                      ");

        List<Player> playersRanked = PositionFactoryUtils.build(doc, KICKER_TABLE_ROWS);
        PositionFactoryUtils.printPlayers(playersRanked);

        System.out.println("                                      ");
        System.out.println("         Done Kicker Rankings         ");
        System.out.println("                                      ");

        return playersRanked;
    }
}
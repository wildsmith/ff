package com.wildsmith.position;

import com.wildsmith.constants.CSSLocationConstants;
import com.wildsmith.objects.Player;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DSTPosition implements PositionFactory {

    private static final String FANTASY_RANKING_DST_URI = "http://www.cbssports.com/fantasy/football/rankings/standard/DST/yearly/";

    private static final List<String> DEFENSIVE_SPECIAL_TEAMS_TABLE_ROWS;

    static {
        List<String> tableRows = new ArrayList<String>();
        tableRows.add(CSSLocationConstants.JAMEY_EISENBERG_TABLE_ROWS);
        tableRows.add(CSSLocationConstants.DAVE_RICHARD_TABLE_ROWS);
        tableRows.add(CSSLocationConstants.HEATH_CUMMINGS_TABLE_ROWS);
        DEFENSIVE_SPECIAL_TEAMS_TABLE_ROWS = Collections.unmodifiableList(tableRows);
    }

    @Override
    public List<Player> build() {
        Document doc = PositionFactoryUtils.getFantasyFootballRankings(FANTASY_RANKING_DST_URI);
        if (doc == null) {
            return null;
        }

        PositionFactoryUtils.printBlock("Building DST Rankings");

        List<Player> playersRanked = PositionFactoryUtils.build(doc, DEFENSIVE_SPECIAL_TEAMS_TABLE_ROWS);
        PositionFactoryUtils.printPlayers(playersRanked);

        PositionFactoryUtils.printBlock("Done DST Rankings");

        return playersRanked;
    }
}
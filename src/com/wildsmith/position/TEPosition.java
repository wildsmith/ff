package com.wildsmith.position;

import com.wildsmith.constants.CSSLocationConstants;
import com.wildsmith.objects.Player;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TEPosition implements PositionFactory {

    private static final String FANTASY_RANKING_TE_URI = "http://www.cbssports.com/fantasy/football/rankings/standard/TE/yearly/";

    private static final List<String> TIGHT_END_TABLE_ROWS;

    static {
        List<String> tableRows = new ArrayList<String>();
        tableRows.add(CSSLocationConstants.JAMEY_EISENBERG_TABLE_ROWS);
        tableRows.add(CSSLocationConstants.DAVE_RICHARD_TABLE_ROWS);
        tableRows.add(CSSLocationConstants.HEATH_CUMMINGS_TABLE_ROWS);
        TIGHT_END_TABLE_ROWS = Collections.unmodifiableList(tableRows);
    }

    @Override
    public List<Player> build() {
        Document doc = PositionFactoryUtils.getFantasyFootballRankings(FANTASY_RANKING_TE_URI);
        if (doc == null) {
            return null;
        }

        PositionFactoryUtils.printBlock("Building Tight End Rankings");

        List<Player> playersRanked = PositionFactoryUtils.build(doc, TIGHT_END_TABLE_ROWS);
        PositionFactoryUtils.printPlayers(playersRanked);

        PositionFactoryUtils.printBlock("Done Tight End Rankings");

        return playersRanked;
    }
}
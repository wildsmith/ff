package com.wildsmith.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wildsmith.position.DSTPosition;
import com.wildsmith.position.KPosition;
import com.wildsmith.position.PositionFactory;
import com.wildsmith.position.QBPosition;
import com.wildsmith.position.RBPosition;
import com.wildsmith.position.TEPosition;
import com.wildsmith.position.WRPosition;

public class CSSLocationConstants {

    public static final List<PositionFactory> POSITION_FACTORIES;

    public static final String JAMEY_EISENBERG_TABLE_ROWS =
            "#player_rank_page_container > div:nth-child(1) > table > tbody > tr:nth-child(-1n+33)";

    public static final String DAVE_RICHARD_TABLE_ROWS =
            "#player_rank_page_container > div:nth-child(2) > table > tbody > tr:nth-child(-1n+33)";

    public static final String HEATH_CUMMINGS_TABLE_ROWS =
            "#player_rank_page_container > div:nth-child(3) > table > tbody > tr:nth-child(-1n+33)";

    public static final String FANTASY_PROS_ADP_URI = "http://www.fantasypros.com/nfl/adp/overall.php";

    public static final String FANTASY_PROS_ADP_TABLE_ROWS = "#data > tbody > tr";

    public static final String THE_HUDDLE_DEPTH_CHART_URI = "http://www.thehuddle.com/2015/nfl/nfl-depth-charts.php";

    static {
        List<PositionFactory> tableRows = new ArrayList<>();
        tableRows.add(new QBPosition());
        tableRows.add(new WRPosition());
        tableRows.add(new RBPosition());
        tableRows.add(new TEPosition());
        tableRows.add(new KPosition());
        tableRows.add(new DSTPosition());
        POSITION_FACTORIES = Collections.unmodifiableList(tableRows);
    }
}
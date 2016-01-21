package com.hkm.dllocker.module;

import com.hkm.dllocker.R;
import com.hkm.layout.Menu.TabIconView;

/**
 * Created by hesk on 21/1/16.
 */
public class UI {
    public static TabIconView.Icon ic_button(String name, String display) {
        if (name.equalsIgnoreCase("newsfeed")) {
            return TabIconView.newVectorIconTab(display, R.drawable.ic_get_pocket, R.drawable.ic_reactions);
        } else if (name.equalsIgnoreCase("categories")) {
            return TabIconView.newVectorIconTab(display, R.drawable.ic_space_rocket, R.drawable.ic_space_rocket);
        } else if (name.equalsIgnoreCase("tv")) {
            return TabIconView.newVectorIconTab(display, R.drawable.ic_cate_active_11t, R.drawable.ic_cate_active_11t);
        } else if (name.equalsIgnoreCase("settings")) {
            return TabIconView.newVectorIconTab(display, R.drawable.ic_cate_active_11t, R.drawable.ic_cate_active_11t);
        } else {
            return TabIconView.newVectorIconTab(display, R.drawable.icon_main_normal_grid, R.drawable.icon_main_selected_grid);
        }
    }

    public static TabIconView.Icon ic_button(int num) {
        switch (num) {
            case 0:
                return TabIconView.newVectorIconTab("Vase", R.drawable.ic_get_pocket, R.drawable.ic_get_pocket_active);

            case 1:
                return TabIconView.newVectorIconTab("Archives", R.drawable.ic_space_rocket, R.drawable.ic_space_rocket_active);

            case 2:
                return TabIconView.newVectorIconTab("Videos", R.drawable.ic_reactions, R.drawable.ic_reactions_active);

            case 3:
                return TabIconView.newVectorIconTab("Settings", R.drawable.ic_cate_active_11t, R.drawable.ic_cate_active_11t);

        }
        return null;
    }

}

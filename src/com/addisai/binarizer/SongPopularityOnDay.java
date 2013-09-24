/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.binarizer;

import java.util.HashMap;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
class SongPopularityOnDay {

    String songId;
    Double popularity;

    public SongPopularityOnDay(String songId, Double popualrity) {
        this.songId = songId;
        this.popularity = popualrity;
    }

    public String getSongId() {
        return songId;
    }

    public Double getPopularity() {
        return popularity;
    }
}

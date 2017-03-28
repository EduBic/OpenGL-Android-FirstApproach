package mprogramming.quadtexture;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 27/03/2017.
 */

public class Texture {

    private Bitmap _bitmap = null;
    private List<Rect> _rects = new ArrayList<>(); // better implment a own rect class
    private List<List<Integer>> _groups;

    public int getRectCount() {
        return _rects.size();
    }

    public Rect getRect(int rectIndex) {
        return _rects.get(rectIndex);
    }

    public void addRect(Rect rect) {
        _rects.add(rect);
    }

    public float leftTopForSubTexture(int index) {
        return 0.0f;
    }


}

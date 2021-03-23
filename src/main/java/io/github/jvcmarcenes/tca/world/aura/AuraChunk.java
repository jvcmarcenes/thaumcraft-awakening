package io.github.jvcmarcenes.tca.world.aura;

import io.github.jvcmarcenes.tca.TCA;

public class AuraChunk {

    private static final int AURA_MAX_BASE = 130;

    private int max = 0;
    private int vis = 0;
    private int flux = 0;

    public AuraChunk() { }

    public AuraChunk(float auraAffinity, boolean tainted) {
        float m;

        m = (TCA.RANDOM.nextFloat() * 2 - 1) * (0.1f / auraAffinity) + 1.0f;
        int _max = (int)(AURA_MAX_BASE * auraAffinity * m);

        m = (TCA.RANDOM.nextFloat() * 2 - 1) * 0.1f;
        int _vis = (int)((0.6 + m) * _max);

        int _flux = 0;

        if (tainted) {
            m = ((TCA.RANDOM.nextFloat() * 2 - 1) * 0.06f) + 0.34f;
            _flux = (int) (m * _vis);
            _vis *= 1 - m;
        }

        setMax(_max);
        setVis(_vis);
        setFlux(_flux);
    }

    public AuraChunk(int max, int vis, int flux) {
        setMax(max);
        setVis(vis);
        setFlux(flux);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int value) {
        max = Math.max(0, value);
    }

    public int getVis() {
        return vis;
    }

    public void setVis(int value) {
        vis = Math.min(max - flux, Math.max(0, value));
    }

    public int getFlux() {
        return flux;
    }

    public void setFlux(int value) {
        this.flux = Math.min(max, Math.max(0, value));
        setVis(vis);
    }

    public void incrementVis(int value) {
        setVis(vis + value);
    }

    public void incrementFlux(int value) {
        setFlux(flux + value);
    }

    public int drainVis(int value) {
        int _vis = vis;
        incrementVis(-value);
        return _vis - vis;
    }

    public int drainFlux(int value) {
        int _flux = flux;
        incrementFlux(-value);
        return _flux - flux;
    }

    public void tick() {
        incrementVis(1);
    }

}

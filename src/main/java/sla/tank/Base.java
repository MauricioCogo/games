package sla.tank;

import javafx.geometry.Rectangle2D;
import sla.api.FX_CG_2D_API;

public interface Base {
    void desenhar(FX_CG_2D_API api);
    void atualizar(FX_CG_2D_API api);
    Rectangle2D getBounds();
}

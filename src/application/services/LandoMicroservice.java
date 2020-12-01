package src.application.services;

import src.MicroService;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    public LandoMicroservice(long duration) {
        super("Lando");
    }

    @Override
    protected void initialize() {
       
    }
}

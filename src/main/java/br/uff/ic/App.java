package br.uff.ic;

public class App {
 
    public static void main(String[] args) {
        WindowFrame wf = new WindowFrame();

        InputProcessor inputProcessor = new InputProcessor();

        inputProcessor.handleInput(args);
    }

}

package br.uff.ic;

public class InputProcessor {

    private Versioner versioner;

    public InputProcessor() {
        this.versioner = new Versioner();
    }
    
    public void handleInput(String[] input) {
        if (input.length > 0) {
            switch (input[0]) {
                case "init":
                    versioner.init();
                    break;
                default:
                    System.out.println("lvn: " + input[0] + " is not a lvn command.");
                    break;
            }
        } else {
            System.out.println("lvn: type a lvn command.");
        }
    }
}

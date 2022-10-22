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
                    if (input.length == 2) {
                        versioner.init(input[1]);
                    } else {
                        versioner.init();
                    }
                    break;
                case "version":
                    if (input.length == 1) {
                        if (versioner.isLvnRepository()) {
                            System.out.println("lvn: missing file for versioning.");
                        }
                    } else if (input.length == 2) {
                        if (versioner.isLvnRepository(input[1])) {
                            versioner.createVersion(input[1]);                        
                        }
                    }
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

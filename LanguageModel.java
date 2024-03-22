import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;



    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		String window = "";
        char c;
        In in = new In(fileName);
        for (int i = 0; i < windowLength; i++) {
            window += in.readChar();

        }

        while (!in.isEmpty()){
            c = in.readChar();
            List prob = CharDataMap.get(window);
            if(prob == null){
                prob = new List();
                CharDataMap.put(window ,prob);
            }
            prob.update(c);
            calculateProbabilities(prob);
            window = window.substring(1) + c;
        }

	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {
		Node curr = probs.getFirstN();
        int count = 0;
        while(curr!= null){
            count += curr.cp.count;
            curr = curr.next;
        }

        curr = probs.getFirstN();

        double commulative = 0;
        while(curr != null){
            curr.cp.p =((curr.cp.count)/(double)count);
            commulative += curr.cp.p;
            curr.cp.cp = commulative;
            curr = curr.next;
        }

	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {

        double rand = randomGenerator.nextDouble();
        Node curr = probs.getFirstN();
        char chr = ' ';
        while(curr != null){
            if(curr.cp.cp >= rand) {
                chr = curr.cp.chr;
                break;
            }
            curr = curr.next;
        }
        return chr;


	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
        if(initialText.length() < windowLength)
            return initialText;
        String window =initialText.substring(initialText.length()-windowLength);
		List list = CharDataMap.get(window);
        StringBuilder gen = new StringBuilder();
        gen.append(window);
        for (int i = 0; i < textLength; i++) {
            gen.append(getRandomChar(list));
            window = gen.substring(gen.length()-windowLength);

            list = CharDataMap.get(window);
        }
        return gen.toString();
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        int windowlength = Integer.parseInt(args[0]);
        String initialtext = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        boolean randomGen = args[3].equals("random");
        String filename = args[4];
        LanguageModel model;
        if(randomGen)
            model = new LanguageModel(windowlength);
        else
            model = new LanguageModel(windowlength , 20);

        model.train(filename);
        System.out.println(model.generate(initialtext,generatedTextLength));


    }


}

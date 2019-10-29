public class Node implements Comparable<Node> {

        int symbol;
        int frequency;
        Node left;
        Node right;
        String code ="";


        public Node getRight() {
            return right;
        }
        public void setRight(Node right) {
            this.right = right;
        }
        public Node getLeft() {
            return left;
        }
        public void setLeft(Node left) {
            this.left = left;
        }

        public  Node (int symbol, int frequency)
        {
            this.symbol = symbol;
            this.frequency =frequency;

        }
        public  Node(int frequency)
        {
            this.frequency =frequency;

        }

        public int getSymbol() {
            return symbol;
        }
        public void setSymbol(int symbol) {
            this.symbol = symbol;
        }
        public int getFrequency() {
            return frequency;
        }
        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
        public boolean equals(Node other)
        {
            return this.frequency==other.frequency;
        }

        @Override
        public int compareTo(Node other) {
            if (this.equals(other))
                return 1;
            else if (getFrequency() > other.frequency)
                return 1;
            else
                return -1;

        }


}

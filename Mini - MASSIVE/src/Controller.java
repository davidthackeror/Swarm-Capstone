public class Controller {

    int algoChoice;

    public void Cont() {
        //placeholder
        setAlgoChoice(0);

        //algorithm switch
        switch (getAlgoChoice()) {
            case 0:
                //goto algo 0
                break;

            case 1:
                //goto algo 1
                break;
            case 2:
                //goto algo 2
                break;
            case 3:
                //goto algo 3
                break;
            default:
                //goto algo 0
        }


    }
    public int getAlgoChoice () {
        return algoChoice;
    }

    public void setAlgoChoice ( int algoChoice){
        this.algoChoice = algoChoice;
    }
}

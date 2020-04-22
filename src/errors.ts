export class OptionsRequiredError extends Error{
    constructor(){
        super("This method requires an options argument");
    }
}

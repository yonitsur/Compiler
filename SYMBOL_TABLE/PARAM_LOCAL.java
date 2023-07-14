package SYMBOL_TABLE;

public class PARAM_LOCAL {
    public enum VAR_TYPE {
        GLOBAL,PARAM,LOCAL,CLASS_FIELD;
    }
    public int varIndex;
    public VAR_TYPE varType;
    public PARAM_LOCAL(int varIndex, VAR_TYPE varType){
        this.varIndex = varIndex;
        this.varType = varType;
    }
    public int getLocalOffset(){
        int direction = 0;
        int start = 0;
        if (varType == PARAM_LOCAL.VAR_TYPE.PARAM) {
            direction = 1;
            start = 8;
        } else if (varType == PARAM_LOCAL.VAR_TYPE.LOCAL) {
            direction = -1;
            start = -4;
        }
        int offset = varIndex; // varIndex is allready absolute value for class fields
        if (varType != PARAM_LOCAL.VAR_TYPE.CLASS_FIELD) {
            offset = start + 4*varIndex*direction;
        }
        
        return offset;
    }
}
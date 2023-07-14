// package IR;
// import TEMP.*;
// import AST.AST_CFIELD_LIST;
// import MIPS.*;
// import TYPES.*;
// public class IRcommand_Class_Field extends IRcommand{
//     String type;
//     String field_name;
//     String class_name;
//     TEMP value;
//     public IRcommand_Class_Field(String type, String field_name, String class_name, TEMP value) {
//         this.type = type;
//         this.field_name = field_name;
//         this.class_name = class_name;
//         this.value = value;
//     }
//     public void MIPSme() {
//         switch (this.type) {
// 			case "string":
//                 MIPSGenerator.getInstance().class_field_string(class_name, field_name, value);
//                 break;
//             case "int":
//                MIPSGenerator.getInstance().class_field_int(class_name, field_name, value);
//                 break;
// 		}
//        // MIPSGenerator.getInstance().class_field(t, field_name);
//     }
    
// }

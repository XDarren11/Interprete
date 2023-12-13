import java.util.List;

import org.xml.sax.Parser;

public class ASDR implements Parser {

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;

    public ASDR(List<Token> tokens) {
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        PROGRAM();

        if (preanalisis.tipo == TipoToken.EOF && !hayErrores) {
            System.out.println("Consulta correcta");
            return true;
        } else {
            System.out.println("Se encontraron errores");
        }

        return false;
    }

    private void PROGRAM() {
        DECLARATION();
    }

    private void DECLARATION() {
        if (hayErrores) {
            return
        }
        if (preanalisis.tipo == TipoToken.FUN) {
            FUN_DECL();
            DECLARATION();
        } else if (preanalisis.tipo == TipoToken.VAR) {
            VAR_DECL();
            DECLARATION();
        } else if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.FOR || preanalisis.tipo == TipoToken.IF || preanalisis.tipo == TipoToken.PRINT ||
        preanalisis.tipo == TipoToken.RETURN || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.LEFT_BRACE || preanalisis.tipo == TipoToken.MINUS ||
        preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING ||
        preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            STATEMENT();
            DECLARATION();
        }
    }

    private void FUN_DECL(){
        if (hayErrores) {
            return
        }
        if (preanalisis.tipo == TipoToken.FUN) {
            match(TipoToken.FUN);
            FUNCTION();
        } else {
            hayErrores = true;
            System.out.println("Error en la linea: " + preanalisis.linea + ", columna: " + preanalisis.columnaE+". Se esperaba 'fun'.");
        }
    }

    private void VAR_DECL() {
        if (hayErrores)
            return;
        if (preanalisis.tipo == TipoToken.VAR) {
            match(TipoToken.VAR);
            if (preanalisis.tipo == TipoToken.IDENTIFIER) {
                match(TipoToken.IDENTIFIER);
                VAR_INIT();
                if (preanalisis.tipo == TipoToken.SEMICOLON) {
                    match(TipoToken.SEMICOLON);
                } else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                            + ". Se esperaba ';'.");
                }
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba 'identifier'.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'var'.");
        }
    }

    private void VAR_INIT(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo == TipoToken.EQUAL) {
            match(TipoToken.EQUAL)
            EXPRESSION();
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '='.");
        }
    }

    private void STATEMENT() {
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE ||
                preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            EXPR_STMT();
        } else if (preanalisis.tipo == TipoToken.FOR) {
            FOR_STMT();
        } else if (preanalisis.tipo == TipoToken.IF) {
            IF_STMT();
        } else if (preanalisis.tipo == TipoToken.PRINT) {
            PRINT_STMT();
        } else if (preanalisis.tipo == TipoToken.RETURN) {
            RETURN_STMT();
        } else if (preanalisis.tipo == TipoToken.WHILE) {
            WHILE_STMT();
        } else if (preanalisis.tipo == TipoToken.LEFT_BRACE) {
            BLOCK();
        }
    }

}

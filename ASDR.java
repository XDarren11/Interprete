import java.util.List;

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

    private void EXPR_STMT(){
        if (hayErrores) {
            return;
        }
        EXPRESSION();
        if (preanalisis.tipo == TipoToken.SEMICOLON) {
            match(preanalisis.tipo == TipoToken.SEMICOLON)
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ';'.");
        }
    }

    private void FOR_STMT() {
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo == TipoToken.FOR) {
            match(TipoToken.FOR);

            if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
                match(TipoToken.LEFT_PAREN);
                FOR_STMT_1();
                FOR_STMT_2();
                FOR_STMT_3();

                if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);
                    STATEMENT();
                }
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'for'.");
        }
    }

    void FOR_STMT_1() {
        if (hayErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.VAR) {
            VAR_DECL();
        } else if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE ||
        preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING ||
        preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            EXPR_STMT();
        } else if (preanalisis.tipo == TipoToken.SEMICOLON) {
            match(TipoToken.SEMICOLON)
        } else{
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE + ". Se esperaba 'var' una 'expresion' o ';'.");
        }
    }

    void FOR_STMT_2(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE ||
        preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING ||
        preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            EXPRESSION();
            if (preanalisis.tipo == TipoToken.SEMICOLON) {
                match(TipoToken.SEMICOLON);
            }
        } else if (preanalisis.tipo == TipoToken.SEMICOLON) {
            match(TipoToken.SEMICOLON)
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE + ". Se esperaba 'for' o ';'.");
        }
    }

    void FOR_STMT_3() {
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE ||
                preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING ||
                preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            EXPRESSION();
        }
    }

    void IF_STMT() {
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo == TipoToken.IF) {
            match(TipoToken.IF);
            if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
                match(TipoToken.LEFT_PAREN);
                EXPRESSION();

                if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);
                    STATEMENT();
                    ELSE_STATEMENT();
                }
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'if'.");
        }
    }

}

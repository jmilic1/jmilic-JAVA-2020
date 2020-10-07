package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw05.db.lexer.*;

/**
 * A parser used for defining query commands and conditional expressions from
 * given input String.
 * 
 * @author Jura Milić
 *
 */
public class QueryParser {
	/**
	 * Lexer used for tokenising
	 */
	private QueryLexer lexer;
	/**
	 * Defined expressions
	 */
	private List<ConditionalExpression> expressions;
	/**
	 * Keeps track if given command is looking for a specific JMBAG
	 */
	private boolean isDirect;
	/**
	 * If isDirect is true, this is the JMBAG that needs to be searched for
	 */
	private String queriedJmbag;

	/**
	 * Constructs a QueryParser with given input query and parses the input. The
	 * input is parsed into a list of expressions.
	 * 
	 * @param query input query
	 */
	public QueryParser(String query) {
		lexer = new QueryLexer(query);
		expressions = new ArrayList<>();
		isDirect = false;
		queriedJmbag = "";

		parse();
	}

	/**
	 * Parses the query
	 */
	public void parse() {
		List<Token> tokens = tokenise(lexer);

		if (tokens.size() == 3 && tokens.get(0).getValue().toString().equals("jmbag")
				&& tokens.get(1).getValue().toString().equals("=")) {
			readDirectQuery(tokens);

		} else {
			for (int i = 0; i < tokens.size(); i++) {
				Token token = tokens.get(i);
				if (token.getType() == TokenType.OPERATOR) {
					readExpression(tokens, i);

					if (i == tokens.size() - 2)
						break;
					if (tokens.get(i + 2).getType() == TokenType.AND) {
						i += 3;
					} else {
						throw new QueryParserException();
					}
				}
			}
		}
	}

	/**
	 * Reads one expression from given list of tokens. Given index points to the
	 * operator of the expression.
	 * 
	 * @param tokens given list
	 * @param i      given index
	 */
	private void readExpression(List<Token> tokens, int i) {
		IFieldValueGetter value1 = readFieldValue(tokens.get(i - 1));
		Token literal = tokens.get(i + 1);

		if (literal.getType() != TokenType.LITERAL)
			throw new QueryParserException();

		String value2 = literal.getValue().toString();

		switch (tokens.get(i).getValue().toString()) {
		case ("<"):
			expressions.add(new ConditionalExpression(value1, value2, ComparisonOperators.LESS));
			break;
		case (">"):
			expressions.add(new ConditionalExpression(value1, value2, ComparisonOperators.GREATER));
			break;
		case ("<="):
			expressions.add(new ConditionalExpression(value1, value2, ComparisonOperators.LESS_OR_EQUALS));
			break;
		case (">="):
			expressions.add(new ConditionalExpression(value1, value2, ComparisonOperators.GREATER_OR_EQUALS));
			break;
		case ("="):
			expressions.add(new ConditionalExpression(value1, value2, ComparisonOperators.EQUALS));
			break;
		case ("!="):
			expressions.add(new ConditionalExpression(value1, value2, ComparisonOperators.NOT_EQUALS));
			break;
		case ("LIKE"):
			expressions.add(new ConditionalExpression(value1, value2, ComparisonOperators.LIKE));
			break;
		}
	}

	/**
	 * Reads which field should be checked in conditional expression from given
	 * Token
	 * 
	 * @param token given Token
	 * @return FieldValueGetter of read field
	 */
	private IFieldValueGetter readFieldValue(Token token) {
		IFieldValueGetter value1 = FieldValueGetters.JMBAG;

		if (token.getType() != TokenType.FIELD)
			throw new QueryParserException();
		switch (token.getValue().toString()) {
		case ("firstName"):
			value1 = FieldValueGetters.FIRST_NAME;
			break;
		case ("lastName"):
			value1 = FieldValueGetters.LAST_NAME;
			break;
		}
		return value1;
	}

	/**
	 * Reads which JMBAG was asked for from given list of tokens
	 * 
	 * @param tokens given list
	 */
	private void readDirectQuery(List<Token> tokens) {
		isDirect = true;
		queriedJmbag = tokens.get(2).getValue().toString();
		expressions.add(new ConditionalExpression(FieldValueGetters.JMBAG, queriedJmbag, ComparisonOperators.EQUALS));
	}

	/**
	 * Tokenise using given lexer.
	 * 
	 * @param lexer given lexer
	 * @return list of tokens
	 */
	private List<Token> tokenise(QueryLexer lexer) {
		List<Token> tokens = new ArrayList<Token>();
		Token token = lexer.nextToken();

		while (token.getType() != TokenType.EOF) {
			switch (token.getType()) {
			case FIELD:
				String value = token.getValue().toString();
				if (!value.equals("jmbag") && !value.equals("lastName") && !value.equals("firstName")) {
					throw new QueryParserException();
				}
				tokens.add(token);
				break;
			default:
				tokens.add(token);
			}
			token = lexer.nextToken();
		}
		return tokens;
	}

	/**
	 * Checks if a direct query was asked for.
	 * 
	 * @return true a direct query was asked for, false otherwise
	 */
	public boolean isDirectQuery() {
		return isDirect;
	}

	/**
	 * @return the JMBAG asked for in direct query
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery())
			throw new IllegalStateException();
		return queriedJmbag;
	}

	/**
	 * @return queried expressions
	 */
	public List<ConditionalExpression> getQuery() {
		return expressions;
	}
}

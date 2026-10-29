package nl.han.ica.icss.parser;

import java.util.Stack;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;
	//Use this to keep track of the parent nodes when recursively traversing the ast
	private final IHANStack<ASTNode> currentContainer = new HANStack<>();

	public ASTListener() {
		ast = new AST();
	}
    public AST getAST() {
        return ast;
    }

	@Override
	public void enterStylerule(ICSSParser.StyleruleContext ctx) {
		Stylerule rule = new Stylerule();
		ast.root.addChild(rule);
		currentContainer.push(rule);
	}

	@Override
	public void exitStylerule(ICSSParser.StyleruleContext ctx) {
		currentContainer.pop();
	}

	@Override
	public void enterSelector(ICSSParser.SelectorContext ctx) {
		Selector selector;

		if (ctx.LOWER_IDENT() != null) {
			selector = new TagSelector(ctx.getText());
		} else if (ctx.ID_IDENT() != null) {
			selector = new IdSelector(ctx.getText());
		} else {
			selector = new ClassSelector(ctx.getText());
		}

		currentContainer.peek().addChild(selector);
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration declaration = new Declaration(ctx.property().getText());

		currentContainer.peek().addChild(declaration);
		currentContainer.push(declaration);
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		currentContainer.pop();
	}
	@Override
	public void enterLiteral(ICSSParser.LiteralContext ctx) {
		Literal literal;

		if (ctx.PIXELSIZE() != null) {
			literal = new PixelLiteral(ctx.getText());
		} else if (ctx.PERCENTAGE() != null) {
			literal = new PercentageLiteral(ctx.getText());
		} else if (ctx.COLOR() != null) {
			literal = new ColorLiteral(ctx.getText());
		} else {
			literal = new ScalarLiteral(ctx.getText());
		}

		currentContainer.peek().addChild(literal);
	}
}
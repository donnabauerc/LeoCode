
import * as vscode from 'vscode';

export function activate(context: vscode.ExtensionContext) {

	console.log('LeoCode is running...');


	let disposable = vscode.commands.registerCommand('leocode.myCommand', () => {
		vscode.window.showInformationMessage('This is my custom Command');
	});

}

// this method is called when your extension is deactivated
export function deactivate() {}

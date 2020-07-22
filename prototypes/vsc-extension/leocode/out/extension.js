"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.deactivate = exports.activate = void 0;
const vscode = require("vscode");
const control_1 = require("./control");
function activate(context) {
    console.log('LeoCode is running...');
    let disposable = vscode.commands.registerCommand('leocode.myCommand', () => {
        vscode.window.showInformationMessage('This is my custom Command');
        control_1.runRequest();
    });
}
exports.activate = activate;
// this method is called when your extension is deactivated
function deactivate() { }
exports.deactivate = deactivate;
//# sourceMappingURL=extension.js.map
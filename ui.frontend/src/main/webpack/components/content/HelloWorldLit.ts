import {LitElement, html, css} from 'lit';
import {customElement,property} from 'lit/decorators.js';

@customElement('hello-world-lit')
export class HelloWorldLit extends LitElement {

    @property()
    text: 'Lit';

    static styles = css`
    span {
    color: green;
    }
    `;

    render() {
        return html`
            <p>Hello World from <span>${this.text}</span></p>
        `;
    }
}

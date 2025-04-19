import{f as Re,r as Ce,_ as _e,p as pe}from"./Popover-uiGA841y.js";import{bq as Ie,br as Ke,bs as Oe,bb as $e,aZ as W,bt as ze,aG as G,a5 as de,O as j,d as I,D as a,N as ae,ae as J,S as B,z as P,C as N,A as D,b2 as he,ao as le,E as ve,H as X,bu as De,a as w,M as be,T as Ae,ah as Z,bf as me,J as H,F as Fe,bv as Te,bg as je,be as Be,b3 as Le,ac as ue,B as z,ai as Me,L as _,bw as Ee,aL as T,ap as re}from"./index-D_-DzWhp.js";import{B as He,V as Ue,a as qe}from"./Follower-CPJzzLbY.js";import{h as ce,c as Ve}from"./create-B6F92WxY.js";import{u as We}from"./use-merged-state-DuV1Lu3L.js";import{c as Ge}from"./create-ref-setter-C4J8sofl.js";function Ze(e={},o){const i=Ie({ctrl:!1,command:!1,win:!1,shift:!1,tab:!1}),{keydown:r,keyup:t}=e,n=d=>{switch(d.key){case"Control":i.ctrl=!0;break;case"Meta":i.command=!0,i.win=!0;break;case"Shift":i.shift=!0;break;case"Tab":i.tab=!0;break}r!==void 0&&Object.keys(r).forEach(y=>{if(y!==d.key)return;const h=r[y];if(typeof h=="function")h(d);else{const{stop:g=!1,prevent:S=!1}=h;g&&d.stopPropagation(),S&&d.preventDefault(),h.handler(d)}})},s=d=>{switch(d.key){case"Control":i.ctrl=!1;break;case"Meta":i.command=!1,i.win=!1;break;case"Shift":i.shift=!1;break;case"Tab":i.tab=!1;break}t!==void 0&&Object.keys(t).forEach(y=>{if(y!==d.key)return;const h=t[y];if(typeof h=="function")h(d);else{const{stop:g=!1,prevent:S=!1}=h;g&&d.stopPropagation(),S&&d.preventDefault(),h.handler(d)}})},c=()=>{(o===void 0||o.value)&&(G("keydown",document,n),G("keyup",document,s)),o!==void 0&&de(o,d=>{d?(G("keydown",document,n),G("keyup",document,s)):(W("keydown",document,n),W("keyup",document,s))})};return Ke()?(Oe(c),$e(()=>{(o===void 0||o.value)&&(W("keydown",document,n),W("keyup",document,s))})):c(),ze(i)}function Je(e,o,i){const r=j(e.value);let t=null;return de(e,n=>{t!==null&&window.clearTimeout(t),n===!0?i&&!i.value?r.value=!0:t=window.setTimeout(()=>{r.value=!0},o):r.value=!1}),r}const Xe=I({name:"ChevronRight",render(){return a("svg",{viewBox:"0 0 16 16",fill:"none",xmlns:"http://www.w3.org/2000/svg"},a("path",{d:"M5.64645 3.14645C5.45118 3.34171 5.45118 3.65829 5.64645 3.85355L9.79289 8L5.64645 12.1464C5.45118 12.3417 5.45118 12.6583 5.64645 12.8536C5.84171 13.0488 6.15829 13.0488 6.35355 12.8536L10.8536 8.35355C11.0488 8.15829 11.0488 7.84171 10.8536 7.64645L6.35355 3.14645C6.15829 2.95118 5.84171 2.95118 5.64645 3.14645Z",fill:"currentColor"}))}}),se=ae("n-dropdown-menu"),Q=ae("n-dropdown"),fe=ae("n-dropdown-option"),we=I({name:"DropdownDivider",props:{clsPrefix:{type:String,required:!0}},render(){return a("div",{class:`${this.clsPrefix}-dropdown-divider`})}}),Qe=I({name:"DropdownGroupHeader",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0}},setup(){const{showIconRef:e,hasSubmenuRef:o}=B(se),{renderLabelRef:i,labelFieldRef:r,nodePropsRef:t,renderOptionRef:n}=B(Q);return{labelField:r,showIcon:e,hasSubmenu:o,renderLabel:i,nodeProps:t,renderOption:n}},render(){var e;const{clsPrefix:o,hasSubmenu:i,showIcon:r,nodeProps:t,renderLabel:n,renderOption:s}=this,{rawNode:c}=this.tmNode,d=a("div",Object.assign({class:`${o}-dropdown-option`},t==null?void 0:t(c)),a("div",{class:`${o}-dropdown-option-body ${o}-dropdown-option-body--group`},a("div",{"data-dropdown-option":!0,class:[`${o}-dropdown-option-body__prefix`,r&&`${o}-dropdown-option-body__prefix--show-icon`]},J(c.icon)),a("div",{class:`${o}-dropdown-option-body__label`,"data-dropdown-option":!0},n?n(c):J((e=c.title)!==null&&e!==void 0?e:c[this.labelField])),a("div",{class:[`${o}-dropdown-option-body__suffix`,i&&`${o}-dropdown-option-body__suffix--has-submenu`],"data-dropdown-option":!0})));return s?s({node:d,option:c}):d}}),Ye=P("icon",`
 height: 1em;
 width: 1em;
 line-height: 1em;
 text-align: center;
 display: inline-block;
 position: relative;
 fill: currentColor;
 transform: translateZ(0);
`,[N("color-transition",{transition:"color .3s var(--n-bezier)"}),N("depth",{color:"var(--n-color)"},[D("svg",{opacity:"var(--n-opacity)",transition:"opacity .3s var(--n-bezier)"})]),D("svg",{height:"1em",width:"1em"})]),en=Object.assign(Object.assign({},X.props),{depth:[String,Number],size:[Number,String],color:String,component:[Object,Function]}),nn=I({_n_icon__:!0,name:"Icon",inheritAttrs:!1,props:en,setup(e){const{mergedClsPrefixRef:o,inlineThemeDisabled:i}=ve(e),r=X("Icon","-icon",Ye,De,e,o),t=w(()=>{const{depth:s}=e,{common:{cubicBezierEaseInOut:c},self:d}=r.value;if(s!==void 0){const{color:y,[`opacity${s}Depth`]:h}=d;return{"--n-bezier":c,"--n-color":y,"--n-opacity":h}}return{"--n-bezier":c,"--n-color":"","--n-opacity":""}}),n=i?be("icon",w(()=>`${e.depth||"d"}`),t,e):void 0;return{mergedClsPrefix:o,mergedStyle:w(()=>{const{size:s,color:c}=e;return{fontSize:Re(s),color:c}}),cssVars:i?void 0:t,themeClass:n==null?void 0:n.themeClass,onRender:n==null?void 0:n.onRender}},render(){var e;const{$parent:o,depth:i,mergedClsPrefix:r,component:t,onRender:n,themeClass:s}=this;return!((e=o==null?void 0:o.$options)===null||e===void 0)&&e._n_icon__&&he("icon","don't wrap `n-icon` inside `n-icon`"),n==null||n(),a("i",le(this.$attrs,{role:"img",class:[`${r}-icon`,s,{[`${r}-icon--depth`]:i,[`${r}-icon--color-transition`]:i!==void 0}],style:[this.cssVars,this.mergedStyle]}),t?a(t):this.$slots)}});function ie(e,o){return e.type==="submenu"||e.type===void 0&&e[o]!==void 0}function on(e){return e.type==="group"}function ye(e){return e.type==="divider"}function tn(e){return e.type==="render"}const ge=I({name:"DropdownOption",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0},parentKey:{type:[String,Number],default:null},placement:{type:String,default:"right-start"},props:Object,scrollable:Boolean},setup(e){const o=B(Q),{hoverKeyRef:i,keyboardKeyRef:r,lastToggledSubmenuKeyRef:t,pendingKeyPathRef:n,activeKeyPathRef:s,animatedRef:c,mergedShowRef:d,renderLabelRef:y,renderIconRef:h,labelFieldRef:g,childrenFieldRef:S,renderOptionRef:R,nodePropsRef:K,menuPropsRef:L}=o,x=B(fe,null),O=B(se),U=B(me),Y=w(()=>e.tmNode.rawNode),q=w(()=>{const{value:u}=S;return ie(e.tmNode.rawNode,u)}),ee=w(()=>{const{disabled:u}=e.tmNode;return u}),ne=w(()=>{if(!q.value)return!1;const{key:u,disabled:m}=e.tmNode;if(m)return!1;const{value:C}=i,{value:A}=r,{value:te}=t,{value:F}=n;return C!==null?F.includes(u):A!==null?F.includes(u)&&F[F.length-1]!==u:te!==null?F.includes(u):!1}),oe=w(()=>r.value===null&&!c.value),V=Je(ne,300,oe),M=w(()=>!!(x!=null&&x.enteringSubmenuRef.value)),E=j(!1);H(fe,{enteringSubmenuRef:E});function $(){E.value=!0}function l(){E.value=!1}function b(){const{parentKey:u,tmNode:m}=e;m.disabled||d.value&&(t.value=u,r.value=null,i.value=m.key)}function p(){const{tmNode:u}=e;u.disabled||d.value&&i.value!==u.key&&b()}function f(u){if(e.tmNode.disabled||!d.value)return;const{relatedTarget:m}=u;m&&!ce({target:m},"dropdownOption")&&!ce({target:m},"scrollbarRail")&&(i.value=null)}function k(){const{value:u}=q,{tmNode:m}=e;d.value&&!u&&!m.disabled&&(o.doSelect(m.key,m.rawNode),o.doUpdateShow(!1))}return{labelField:g,renderLabel:y,renderIcon:h,siblingHasIcon:O.showIconRef,siblingHasSubmenu:O.hasSubmenuRef,menuProps:L,popoverBody:U,animated:c,mergedShowSubmenu:w(()=>V.value&&!M.value),rawNode:Y,hasSubmenu:q,pending:Z(()=>{const{value:u}=n,{key:m}=e.tmNode;return u.includes(m)}),childActive:Z(()=>{const{value:u}=s,{key:m}=e.tmNode,C=u.findIndex(A=>m===A);return C===-1?!1:C<u.length-1}),active:Z(()=>{const{value:u}=s,{key:m}=e.tmNode,C=u.findIndex(A=>m===A);return C===-1?!1:C===u.length-1}),mergedDisabled:ee,renderOption:R,nodeProps:K,handleClick:k,handleMouseMove:p,handleMouseEnter:b,handleMouseLeave:f,handleSubmenuBeforeEnter:$,handleSubmenuAfterEnter:l}},render(){var e,o;const{animated:i,rawNode:r,mergedShowSubmenu:t,clsPrefix:n,siblingHasIcon:s,siblingHasSubmenu:c,renderLabel:d,renderIcon:y,renderOption:h,nodeProps:g,props:S,scrollable:R}=this;let K=null;if(t){const U=(e=this.menuProps)===null||e===void 0?void 0:e.call(this,r,r.children);K=a(xe,Object.assign({},U,{clsPrefix:n,scrollable:this.scrollable,tmNodes:this.tmNode.children,parentKey:this.tmNode.key}))}const L={class:[`${n}-dropdown-option-body`,this.pending&&`${n}-dropdown-option-body--pending`,this.active&&`${n}-dropdown-option-body--active`,this.childActive&&`${n}-dropdown-option-body--child-active`,this.mergedDisabled&&`${n}-dropdown-option-body--disabled`],onMousemove:this.handleMouseMove,onMouseenter:this.handleMouseEnter,onMouseleave:this.handleMouseLeave,onClick:this.handleClick},x=g==null?void 0:g(r),O=a("div",Object.assign({class:[`${n}-dropdown-option`,x==null?void 0:x.class],"data-dropdown-option":!0},x),a("div",le(L,S),[a("div",{class:[`${n}-dropdown-option-body__prefix`,s&&`${n}-dropdown-option-body__prefix--show-icon`]},[y?y(r):J(r.icon)]),a("div",{"data-dropdown-option":!0,class:`${n}-dropdown-option-body__label`},d?d(r):J((o=r[this.labelField])!==null&&o!==void 0?o:r.title)),a("div",{"data-dropdown-option":!0,class:[`${n}-dropdown-option-body__suffix`,c&&`${n}-dropdown-option-body__suffix--has-submenu`]},this.hasSubmenu?a(nn,null,{default:()=>a(Xe,null)}):null)]),this.hasSubmenu?a(He,null,{default:()=>[a(Ue,null,{default:()=>a("div",{class:`${n}-dropdown-offset-container`},a(qe,{show:this.mergedShowSubmenu,placement:this.placement,to:R&&this.popoverBody||void 0,teleportDisabled:!R},{default:()=>a("div",{class:`${n}-dropdown-menu-wrapper`},i?a(Ae,{onBeforeEnter:this.handleSubmenuBeforeEnter,onAfterEnter:this.handleSubmenuAfterEnter,name:"fade-in-scale-up-transition",appear:!0},{default:()=>K}):K)}))})]}):null);return h?h({node:O,option:r}):O}}),rn=I({name:"NDropdownGroup",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0},parentKey:{type:[String,Number],default:null}},render(){const{tmNode:e,parentKey:o,clsPrefix:i}=this,{children:r}=e;return a(Fe,null,a(Qe,{clsPrefix:i,tmNode:e,key:e.key}),r==null?void 0:r.map(t=>{const{rawNode:n}=t;return n.show===!1?null:ye(n)?a(we,{clsPrefix:i,key:t.key}):t.isGroup?(he("dropdown","`group` node is not allowed to be put in `group` node."),null):a(ge,{clsPrefix:i,tmNode:t,parentKey:o,key:t.key})}))}}),dn=I({name:"DropdownRenderOption",props:{tmNode:{type:Object,required:!0}},render(){const{rawNode:{render:e,props:o}}=this.tmNode;return a("div",o,[e==null?void 0:e()])}}),xe=I({name:"DropdownMenu",props:{scrollable:Boolean,showArrow:Boolean,arrowStyle:[String,Object],clsPrefix:{type:String,required:!0},tmNodes:{type:Array,default:()=>[]},parentKey:{type:[String,Number],default:null}},setup(e){const{renderIconRef:o,childrenFieldRef:i}=B(Q);H(se,{showIconRef:w(()=>{const t=o.value;return e.tmNodes.some(n=>{var s;if(n.isGroup)return(s=n.children)===null||s===void 0?void 0:s.some(({rawNode:d})=>t?t(d):d.icon);const{rawNode:c}=n;return t?t(c):c.icon})}),hasSubmenuRef:w(()=>{const{value:t}=i;return e.tmNodes.some(n=>{var s;if(n.isGroup)return(s=n.children)===null||s===void 0?void 0:s.some(({rawNode:d})=>ie(d,t));const{rawNode:c}=n;return ie(c,t)})})});const r=j(null);return H(je,null),H(Be,null),H(me,r),{bodyRef:r}},render(){const{parentKey:e,clsPrefix:o,scrollable:i}=this,r=this.tmNodes.map(t=>{const{rawNode:n}=t;return n.show===!1?null:tn(n)?a(dn,{tmNode:t,key:t.key}):ye(n)?a(we,{clsPrefix:o,key:t.key}):on(n)?a(rn,{clsPrefix:o,tmNode:t,parentKey:e,key:t.key}):a(ge,{clsPrefix:o,tmNode:t,parentKey:e,key:t.key,props:n.props,scrollable:i})});return a("div",{class:[`${o}-dropdown-menu`,i&&`${o}-dropdown-menu--scrollable`],ref:"bodyRef"},i?a(Te,{contentClass:`${o}-dropdown-menu__content`},{default:()=>r}):r,this.showArrow?Ce({clsPrefix:o,arrowStyle:this.arrowStyle,arrowClass:void 0,arrowWrapperClass:void 0,arrowWrapperStyle:void 0}):null)}}),an=P("dropdown-menu",`
 transform-origin: var(--v-transform-origin);
 background-color: var(--n-color);
 border-radius: var(--n-border-radius);
 box-shadow: var(--n-box-shadow);
 position: relative;
 transition:
 background-color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier);
`,[Le(),P("dropdown-option",`
 position: relative;
 `,[D("a",`
 text-decoration: none;
 color: inherit;
 outline: none;
 `,[D("&::before",`
 content: "";
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `)]),P("dropdown-option-body",`
 display: flex;
 cursor: pointer;
 position: relative;
 height: var(--n-option-height);
 line-height: var(--n-option-height);
 font-size: var(--n-font-size);
 color: var(--n-option-text-color);
 transition: color .3s var(--n-bezier);
 `,[D("&::before",`
 content: "";
 position: absolute;
 top: 0;
 bottom: 0;
 left: 4px;
 right: 4px;
 transition: background-color .3s var(--n-bezier);
 border-radius: var(--n-border-radius);
 `),ue("disabled",[N("pending",`
 color: var(--n-option-text-color-hover);
 `,[z("prefix, suffix",`
 color: var(--n-option-text-color-hover);
 `),D("&::before","background-color: var(--n-option-color-hover);")]),N("active",`
 color: var(--n-option-text-color-active);
 `,[z("prefix, suffix",`
 color: var(--n-option-text-color-active);
 `),D("&::before","background-color: var(--n-option-color-active);")]),N("child-active",`
 color: var(--n-option-text-color-child-active);
 `,[z("prefix, suffix",`
 color: var(--n-option-text-color-child-active);
 `)])]),N("disabled",`
 cursor: not-allowed;
 opacity: var(--n-option-opacity-disabled);
 `),N("group",`
 font-size: calc(var(--n-font-size) - 1px);
 color: var(--n-group-header-text-color);
 `,[z("prefix",`
 width: calc(var(--n-option-prefix-width) / 2);
 `,[N("show-icon",`
 width: calc(var(--n-option-icon-prefix-width) / 2);
 `)])]),z("prefix",`
 width: var(--n-option-prefix-width);
 display: flex;
 justify-content: center;
 align-items: center;
 color: var(--n-prefix-color);
 transition: color .3s var(--n-bezier);
 z-index: 1;
 `,[N("show-icon",`
 width: var(--n-option-icon-prefix-width);
 `),P("icon",`
 font-size: var(--n-option-icon-size);
 `)]),z("label",`
 white-space: nowrap;
 flex: 1;
 z-index: 1;
 `),z("suffix",`
 box-sizing: border-box;
 flex-grow: 0;
 flex-shrink: 0;
 display: flex;
 justify-content: flex-end;
 align-items: center;
 min-width: var(--n-option-suffix-width);
 padding: 0 8px;
 transition: color .3s var(--n-bezier);
 color: var(--n-suffix-color);
 z-index: 1;
 `,[N("has-submenu",`
 width: var(--n-option-icon-suffix-width);
 `),P("icon",`
 font-size: var(--n-option-icon-size);
 `)]),P("dropdown-menu","pointer-events: all;")]),P("dropdown-offset-container",`
 pointer-events: none;
 position: absolute;
 left: 0;
 right: 0;
 top: -4px;
 bottom: -4px;
 `)]),P("dropdown-divider",`
 transition: background-color .3s var(--n-bezier);
 background-color: var(--n-divider-color);
 height: 1px;
 margin: 4px 0;
 `),P("dropdown-menu-wrapper",`
 transform-origin: var(--v-transform-origin);
 width: fit-content;
 `),D(">",[P("scrollbar",`
 height: inherit;
 max-height: inherit;
 `)]),ue("scrollable",`
 padding: var(--n-padding);
 `),N("scrollable",[z("content",`
 padding: var(--n-padding);
 `)])]),ln={animated:{type:Boolean,default:!0},keyboard:{type:Boolean,default:!0},size:{type:String,default:"medium"},inverted:Boolean,placement:{type:String,default:"bottom"},onSelect:[Function,Array],options:{type:Array,default:()=>[]},menuProps:Function,showArrow:Boolean,renderLabel:Function,renderIcon:Function,renderOption:Function,nodeProps:Function,labelField:{type:String,default:"label"},keyField:{type:String,default:"key"},childrenField:{type:String,default:"children"},value:[String,Number]},sn=Object.keys(pe),un=Object.assign(Object.assign(Object.assign({},pe),ln),X.props),mn=I({name:"Dropdown",inheritAttrs:!1,props:un,setup(e){const o=j(!1),i=We(_(e,"show"),o),r=w(()=>{const{keyField:l,childrenField:b}=e;return Ve(e.options,{getKey(p){return p[l]},getDisabled(p){return p.disabled===!0},getIgnored(p){return p.type==="divider"||p.type==="render"},getChildren(p){return p[b]}})}),t=w(()=>r.value.treeNodes),n=j(null),s=j(null),c=j(null),d=w(()=>{var l,b,p;return(p=(b=(l=n.value)!==null&&l!==void 0?l:s.value)!==null&&b!==void 0?b:c.value)!==null&&p!==void 0?p:null}),y=w(()=>r.value.getPath(d.value).keyPath),h=w(()=>r.value.getPath(e.value).keyPath),g=Z(()=>e.keyboard&&i.value);Ze({keydown:{ArrowUp:{prevent:!0,handler:ee},ArrowRight:{prevent:!0,handler:q},ArrowDown:{prevent:!0,handler:ne},ArrowLeft:{prevent:!0,handler:Y},Enter:{prevent:!0,handler:oe},Escape:U}},g);const{mergedClsPrefixRef:S,inlineThemeDisabled:R}=ve(e),K=X("Dropdown","-dropdown",an,Ee,e,S);H(Q,{labelFieldRef:_(e,"labelField"),childrenFieldRef:_(e,"childrenField"),renderLabelRef:_(e,"renderLabel"),renderIconRef:_(e,"renderIcon"),hoverKeyRef:n,keyboardKeyRef:s,lastToggledSubmenuKeyRef:c,pendingKeyPathRef:y,activeKeyPathRef:h,animatedRef:_(e,"animated"),mergedShowRef:i,nodePropsRef:_(e,"nodeProps"),renderOptionRef:_(e,"renderOption"),menuPropsRef:_(e,"menuProps"),doSelect:L,doUpdateShow:x}),de(i,l=>{!e.animated&&!l&&O()});function L(l,b){const{onSelect:p}=e;p&&re(p,l,b)}function x(l){const{"onUpdate:show":b,onUpdateShow:p}=e;b&&re(b,l),p&&re(p,l),o.value=l}function O(){n.value=null,s.value=null,c.value=null}function U(){x(!1)}function Y(){M("left")}function q(){M("right")}function ee(){M("up")}function ne(){M("down")}function oe(){const l=V();l!=null&&l.isLeaf&&i.value&&(L(l.key,l.rawNode),x(!1))}function V(){var l;const{value:b}=r,{value:p}=d;return!b||p===null?null:(l=b.getNode(p))!==null&&l!==void 0?l:null}function M(l){const{value:b}=d,{value:{getFirstAvailableNode:p}}=r;let f=null;if(b===null){const k=p();k!==null&&(f=k.key)}else{const k=V();if(k){let u;switch(l){case"down":u=k.getNext();break;case"up":u=k.getPrev();break;case"right":u=k.getChild();break;case"left":u=k.getParent();break}u&&(f=u.key)}}f!==null&&(n.value=null,s.value=f)}const E=w(()=>{const{size:l,inverted:b}=e,{common:{cubicBezierEaseInOut:p},self:f}=K.value,{padding:k,dividerColor:u,borderRadius:m,optionOpacityDisabled:C,[T("optionIconSuffixWidth",l)]:A,[T("optionSuffixWidth",l)]:te,[T("optionIconPrefixWidth",l)]:F,[T("optionPrefixWidth",l)]:Se,[T("fontSize",l)]:ke,[T("optionHeight",l)]:Pe,[T("optionIconSize",l)]:Ne}=f,v={"--n-bezier":p,"--n-font-size":ke,"--n-padding":k,"--n-border-radius":m,"--n-option-height":Pe,"--n-option-prefix-width":Se,"--n-option-icon-prefix-width":F,"--n-option-suffix-width":te,"--n-option-icon-suffix-width":A,"--n-option-icon-size":Ne,"--n-divider-color":u,"--n-option-opacity-disabled":C};return b?(v["--n-color"]=f.colorInverted,v["--n-option-color-hover"]=f.optionColorHoverInverted,v["--n-option-color-active"]=f.optionColorActiveInverted,v["--n-option-text-color"]=f.optionTextColorInverted,v["--n-option-text-color-hover"]=f.optionTextColorHoverInverted,v["--n-option-text-color-active"]=f.optionTextColorActiveInverted,v["--n-option-text-color-child-active"]=f.optionTextColorChildActiveInverted,v["--n-prefix-color"]=f.prefixColorInverted,v["--n-suffix-color"]=f.suffixColorInverted,v["--n-group-header-text-color"]=f.groupHeaderTextColorInverted):(v["--n-color"]=f.color,v["--n-option-color-hover"]=f.optionColorHover,v["--n-option-color-active"]=f.optionColorActive,v["--n-option-text-color"]=f.optionTextColor,v["--n-option-text-color-hover"]=f.optionTextColorHover,v["--n-option-text-color-active"]=f.optionTextColorActive,v["--n-option-text-color-child-active"]=f.optionTextColorChildActive,v["--n-prefix-color"]=f.prefixColor,v["--n-suffix-color"]=f.suffixColor,v["--n-group-header-text-color"]=f.groupHeaderTextColor),v}),$=R?be("dropdown",w(()=>`${e.size[0]}${e.inverted?"i":""}`),E,e):void 0;return{mergedClsPrefix:S,mergedTheme:K,tmNodes:t,mergedShow:i,handleAfterLeave:()=>{e.animated&&O()},doUpdateShow:x,cssVars:R?void 0:E,themeClass:$==null?void 0:$.themeClass,onRender:$==null?void 0:$.onRender}},render(){const e=(r,t,n,s,c)=>{var d;const{mergedClsPrefix:y,menuProps:h}=this;(d=this.onRender)===null||d===void 0||d.call(this);const g=(h==null?void 0:h(void 0,this.tmNodes.map(R=>R.rawNode)))||{},S={ref:Ge(t),class:[r,`${y}-dropdown`,this.themeClass],clsPrefix:y,tmNodes:this.tmNodes,style:[...n,this.cssVars],showArrow:this.showArrow,arrowStyle:this.arrowStyle,scrollable:this.scrollable,onMouseenter:s,onMouseleave:c};return a(xe,le(this.$attrs,S,g))},{mergedTheme:o}=this,i={show:this.mergedShow,theme:o.peers.Popover,themeOverrides:o.peerOverrides.Popover,internalOnAfterLeave:this.handleAfterLeave,internalRenderBody:e,onUpdateShow:this.doUpdateShow,"onUpdate:show":void 0};return a(_e,Object.assign({},Me(this.$props,sn),i),{trigger:()=>{var r,t;return(t=(r=this.$slots).default)===null||t===void 0?void 0:t.call(r)}})}});export{Xe as C,mn as _};

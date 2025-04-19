import{d as T,D as h,N as U,A as x,z as g,C as z,ac as k,B as p,ad as He,ae as O,af as _e,S as F,a as b,ag as ee,ah as Q,F as Ee,J as L,ai as J,O as M,aj as Fe,ak as Te,E as Me,H as he,al as Oe,am as de,L as se,M as Ke,an as ke,ao as Le,ap as _}from"./index-D_-DzWhp.js";import{_ as $e}from"./Tooltip-D7FLeIpa.js";import{_ as je}from"./Dropdown-BfCXARAG.js";import{c as Z}from"./create-B6F92WxY.js";import{u as ue}from"./use-merged-state-DuV1Lu3L.js";import{u as Be}from"./use-compitable-Cr7qxnTd.js";import{V as Ve}from"./index-CxXG09Nk.js";const De=T({name:"ChevronDownFilled",render(){return h("svg",{viewBox:"0 0 16 16",fill:"none",xmlns:"http://www.w3.org/2000/svg"},h("path",{d:"M3.20041 5.73966C3.48226 5.43613 3.95681 5.41856 4.26034 5.70041L8 9.22652L11.7397 5.70041C12.0432 5.41856 12.5177 5.43613 12.7996 5.73966C13.0815 6.0432 13.0639 6.51775 12.7603 6.7996L8.51034 10.7996C8.22258 11.0668 7.77743 11.0668 7.48967 10.7996L3.23966 6.7996C2.93613 6.51775 2.91856 6.0432 3.20041 5.73966Z",fill:"currentColor"}))}}),Ue=U("n-layout-sider"),$=U("n-menu"),oe=U("n-submenu"),te=U("n-menu-item-group"),me=[x("&::before","background-color: var(--n-item-color-hover);"),p("arrow",`
 color: var(--n-arrow-color-hover);
 `),p("icon",`
 color: var(--n-item-icon-color-hover);
 `),g("menu-item-content-header",`
 color: var(--n-item-text-color-hover);
 `,[x("a",`
 color: var(--n-item-text-color-hover);
 `),p("extra",`
 color: var(--n-item-text-color-hover);
 `)])],ve=[p("icon",`
 color: var(--n-item-icon-color-hover-horizontal);
 `),g("menu-item-content-header",`
 color: var(--n-item-text-color-hover-horizontal);
 `,[x("a",`
 color: var(--n-item-text-color-hover-horizontal);
 `),p("extra",`
 color: var(--n-item-text-color-hover-horizontal);
 `)])],Ge=x([g("menu",`
 background-color: var(--n-color);
 color: var(--n-item-text-color);
 overflow: hidden;
 transition: background-color .3s var(--n-bezier);
 box-sizing: border-box;
 font-size: var(--n-font-size);
 padding-bottom: 6px;
 `,[z("horizontal",`
 max-width: 100%;
 width: 100%;
 display: flex;
 overflow: hidden;
 padding-bottom: 0;
 `,[g("submenu","margin: 0;"),g("menu-item","margin: 0;"),g("menu-item-content",`
 padding: 0 20px;
 border-bottom: 2px solid #0000;
 `,[x("&::before","display: none;"),z("selected","border-bottom: 2px solid var(--n-border-color-horizontal)")]),g("menu-item-content",[z("selected",[p("icon","color: var(--n-item-icon-color-active-horizontal);"),g("menu-item-content-header",`
 color: var(--n-item-text-color-active-horizontal);
 `,[x("a","color: var(--n-item-text-color-active-horizontal);"),p("extra","color: var(--n-item-text-color-active-horizontal);")])]),z("child-active",`
 border-bottom: 2px solid var(--n-border-color-horizontal);
 `,[g("menu-item-content-header",`
 color: var(--n-item-text-color-child-active-horizontal);
 `,[x("a",`
 color: var(--n-item-text-color-child-active-horizontal);
 `),p("extra",`
 color: var(--n-item-text-color-child-active-horizontal);
 `)]),p("icon",`
 color: var(--n-item-icon-color-child-active-horizontal);
 `)]),k("disabled",[k("selected, child-active",[x("&:focus-within",ve)]),z("selected",[E(null,[p("icon","color: var(--n-item-icon-color-active-hover-horizontal);"),g("menu-item-content-header",`
 color: var(--n-item-text-color-active-hover-horizontal);
 `,[x("a","color: var(--n-item-text-color-active-hover-horizontal);"),p("extra","color: var(--n-item-text-color-active-hover-horizontal);")])])]),z("child-active",[E(null,[p("icon","color: var(--n-item-icon-color-child-active-hover-horizontal);"),g("menu-item-content-header",`
 color: var(--n-item-text-color-child-active-hover-horizontal);
 `,[x("a","color: var(--n-item-text-color-child-active-hover-horizontal);"),p("extra","color: var(--n-item-text-color-child-active-hover-horizontal);")])])]),E("border-bottom: 2px solid var(--n-border-color-horizontal);",ve)]),g("menu-item-content-header",[x("a","color: var(--n-item-text-color-horizontal);")])])]),k("responsive",[g("menu-item-content-header",`
 overflow: hidden;
 text-overflow: ellipsis;
 `)]),z("collapsed",[g("menu-item-content",[z("selected",[x("&::before",`
 background-color: var(--n-item-color-active-collapsed) !important;
 `)]),g("menu-item-content-header","opacity: 0;"),p("arrow","opacity: 0;"),p("icon","color: var(--n-item-icon-color-collapsed);")])]),g("menu-item",`
 height: var(--n-item-height);
 margin-top: 6px;
 position: relative;
 `),g("menu-item-content",`
 box-sizing: border-box;
 line-height: 1.75;
 height: 100%;
 display: grid;
 grid-template-areas: "icon content arrow";
 grid-template-columns: auto 1fr auto;
 align-items: center;
 cursor: pointer;
 position: relative;
 padding-right: 18px;
 transition:
 background-color .3s var(--n-bezier),
 padding-left .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 `,[x("> *","z-index: 1;"),x("&::before",`
 z-index: auto;
 content: "";
 background-color: #0000;
 position: absolute;
 left: 8px;
 right: 8px;
 top: 0;
 bottom: 0;
 pointer-events: none;
 border-radius: var(--n-border-radius);
 transition: background-color .3s var(--n-bezier);
 `),z("disabled",`
 opacity: .45;
 cursor: not-allowed;
 `),z("collapsed",[p("arrow","transform: rotate(0);")]),z("selected",[x("&::before","background-color: var(--n-item-color-active);"),p("arrow","color: var(--n-arrow-color-active);"),p("icon","color: var(--n-item-icon-color-active);"),g("menu-item-content-header",`
 color: var(--n-item-text-color-active);
 `,[x("a","color: var(--n-item-text-color-active);"),p("extra","color: var(--n-item-text-color-active);")])]),z("child-active",[g("menu-item-content-header",`
 color: var(--n-item-text-color-child-active);
 `,[x("a",`
 color: var(--n-item-text-color-child-active);
 `),p("extra",`
 color: var(--n-item-text-color-child-active);
 `)]),p("arrow",`
 color: var(--n-arrow-color-child-active);
 `),p("icon",`
 color: var(--n-item-icon-color-child-active);
 `)]),k("disabled",[k("selected, child-active",[x("&:focus-within",me)]),z("selected",[E(null,[p("arrow","color: var(--n-arrow-color-active-hover);"),p("icon","color: var(--n-item-icon-color-active-hover);"),g("menu-item-content-header",`
 color: var(--n-item-text-color-active-hover);
 `,[x("a","color: var(--n-item-text-color-active-hover);"),p("extra","color: var(--n-item-text-color-active-hover);")])])]),z("child-active",[E(null,[p("arrow","color: var(--n-arrow-color-child-active-hover);"),p("icon","color: var(--n-item-icon-color-child-active-hover);"),g("menu-item-content-header",`
 color: var(--n-item-text-color-child-active-hover);
 `,[x("a","color: var(--n-item-text-color-child-active-hover);"),p("extra","color: var(--n-item-text-color-child-active-hover);")])])]),z("selected",[E(null,[x("&::before","background-color: var(--n-item-color-active-hover);")])]),E(null,me)]),p("icon",`
 grid-area: icon;
 color: var(--n-item-icon-color);
 transition:
 color .3s var(--n-bezier),
 font-size .3s var(--n-bezier),
 margin-right .3s var(--n-bezier);
 box-sizing: content-box;
 display: inline-flex;
 align-items: center;
 justify-content: center;
 `),p("arrow",`
 grid-area: arrow;
 font-size: 16px;
 color: var(--n-arrow-color);
 transform: rotate(180deg);
 opacity: 1;
 transition:
 color .3s var(--n-bezier),
 transform 0.2s var(--n-bezier),
 opacity 0.2s var(--n-bezier);
 `),g("menu-item-content-header",`
 grid-area: content;
 transition:
 color .3s var(--n-bezier),
 opacity .3s var(--n-bezier);
 opacity: 1;
 white-space: nowrap;
 color: var(--n-item-text-color);
 `,[x("a",`
 outline: none;
 text-decoration: none;
 transition: color .3s var(--n-bezier);
 color: var(--n-item-text-color);
 `,[x("&::before",`
 content: "";
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `)]),p("extra",`
 font-size: .93em;
 color: var(--n-group-text-color);
 transition: color .3s var(--n-bezier);
 `)])]),g("submenu",`
 cursor: pointer;
 position: relative;
 margin-top: 6px;
 `,[g("menu-item-content",`
 height: var(--n-item-height);
 `),g("submenu-children",`
 overflow: hidden;
 padding: 0;
 `,[He({duration:".2s"})])]),g("menu-item-group",[g("menu-item-group-title",`
 margin-top: 6px;
 color: var(--n-group-text-color);
 cursor: default;
 font-size: .93em;
 height: 36px;
 display: flex;
 align-items: center;
 transition:
 padding-left .3s var(--n-bezier),
 color .3s var(--n-bezier);
 `)])]),g("menu-tooltip",[x("a",`
 color: inherit;
 text-decoration: none;
 `)]),g("menu-divider",`
 transition: background-color .3s var(--n-bezier);
 background-color: var(--n-divider-color);
 height: 1px;
 margin: 6px 18px;
 `)]);function E(e,r){return[z("hover",e,r),x("&:hover",e,r)]}const fe=T({name:"MenuOptionContent",props:{collapsed:Boolean,disabled:Boolean,title:[String,Function],icon:Function,extra:[String,Function],showArrow:Boolean,childActive:Boolean,hover:Boolean,paddingLeft:Number,selected:Boolean,maxIconSize:{type:Number,required:!0},activeIconSize:{type:Number,required:!0},iconMarginRight:{type:Number,required:!0},clsPrefix:{type:String,required:!0},onClick:Function,tmNode:{type:Object,required:!0},isEllipsisPlaceholder:Boolean},setup(e){const{props:r}=F($);return{menuProps:r,style:b(()=>{const{paddingLeft:n}=e;return{paddingLeft:n&&`${n}px`}}),iconStyle:b(()=>{const{maxIconSize:n,activeIconSize:c,iconMarginRight:a}=e;return{width:`${n}px`,height:`${n}px`,fontSize:`${c}px`,marginRight:`${a}px`}})}},render(){const{clsPrefix:e,tmNode:r,menuProps:{renderIcon:n,renderLabel:c,renderExtra:a,expandIcon:d}}=this,s=n?n(r.rawNode):O(this.icon);return h("div",{onClick:f=>{var u;(u=this.onClick)===null||u===void 0||u.call(this,f)},role:"none",class:[`${e}-menu-item-content`,{[`${e}-menu-item-content--selected`]:this.selected,[`${e}-menu-item-content--collapsed`]:this.collapsed,[`${e}-menu-item-content--child-active`]:this.childActive,[`${e}-menu-item-content--disabled`]:this.disabled,[`${e}-menu-item-content--hover`]:this.hover}],style:this.style},s&&h("div",{class:`${e}-menu-item-content__icon`,style:this.iconStyle,role:"none"},[s]),h("div",{class:`${e}-menu-item-content-header`,role:"none"},this.isEllipsisPlaceholder?this.title:c?c(r.rawNode):O(this.title),this.extra||a?h("span",{class:`${e}-menu-item-content-header__extra`}," ",a?a(r.rawNode):O(this.extra)):null),this.showArrow?h(_e,{ariaHidden:!0,class:`${e}-menu-item-content__arrow`,clsPrefix:e},{default:()=>d?d(r.rawNode):h(De,null)}):null)}}),D=8;function ne(e){const r=F($),{props:n,mergedCollapsedRef:c}=r,a=F(oe,null),d=F(te,null),s=b(()=>n.mode==="horizontal"),f=b(()=>s.value?n.dropdownPlacement:"tmNodes"in e?"right-start":"right"),u=b(()=>{var m;return Math.max((m=n.collapsedIconSize)!==null&&m!==void 0?m:n.iconSize,n.iconSize)}),I=b(()=>{var m;return!s.value&&e.root&&c.value&&(m=n.collapsedIconSize)!==null&&m!==void 0?m:n.iconSize}),N=b(()=>{if(s.value)return;const{collapsedWidth:m,indent:w,rootIndent:A}=n,{root:R,isGroup:P}=e,H=A===void 0?w:A;return R?c.value?m/2-u.value/2:H:d&&typeof d.paddingLeftRef.value=="number"?w/2+d.paddingLeftRef.value:a&&typeof a.paddingLeftRef.value=="number"?(P?w/2:w)+a.paddingLeftRef.value:0}),y=b(()=>{const{collapsedWidth:m,indent:w,rootIndent:A}=n,{value:R}=u,{root:P}=e;return s.value||!P||!c.value?D:(A===void 0?w:A)+R+D-(m+R)/2});return{dropdownPlacement:f,activeIconSize:I,maxIconSize:u,paddingLeft:N,iconMarginRight:y,NMenu:r,NSubmenu:a}}const re={internalKey:{type:[String,Number],required:!0},root:Boolean,isGroup:Boolean,level:{type:Number,required:!0},title:[String,Function],extra:[String,Function]},qe=T({name:"MenuDivider",setup(){const e=F($),{mergedClsPrefixRef:r,isHorizontalRef:n}=e;return()=>n.value?null:h("div",{class:`${r.value}-menu-divider`})}}),pe=Object.assign(Object.assign({},re),{tmNode:{type:Object,required:!0},disabled:Boolean,icon:Function,onClick:Function}),We=ee(pe),Je=T({name:"MenuOption",props:pe,setup(e){const r=ne(e),{NSubmenu:n,NMenu:c}=r,{props:a,mergedClsPrefixRef:d,mergedCollapsedRef:s}=c,f=n?n.mergedDisabledRef:{value:!1},u=b(()=>f.value||e.disabled);function I(y){const{onClick:m}=e;m&&m(y)}function N(y){u.value||(c.doSelect(e.internalKey,e.tmNode.rawNode),I(y))}return{mergedClsPrefix:d,dropdownPlacement:r.dropdownPlacement,paddingLeft:r.paddingLeft,iconMarginRight:r.iconMarginRight,maxIconSize:r.maxIconSize,activeIconSize:r.activeIconSize,mergedTheme:c.mergedThemeRef,menuProps:a,dropdownEnabled:Q(()=>e.root&&s.value&&a.mode!=="horizontal"&&!u.value),selected:Q(()=>c.mergedValueRef.value===e.internalKey),mergedDisabled:u,handleClick:N}},render(){const{mergedClsPrefix:e,mergedTheme:r,tmNode:n,menuProps:{renderLabel:c,nodeProps:a}}=this,d=a==null?void 0:a(n.rawNode);return h("div",Object.assign({},d,{role:"menuitem",class:[`${e}-menu-item`,d==null?void 0:d.class]}),h($e,{theme:r.peers.Tooltip,themeOverrides:r.peerOverrides.Tooltip,trigger:"hover",placement:this.dropdownPlacement,disabled:!this.dropdownEnabled||this.title===void 0,internalExtraClass:["menu-tooltip"]},{default:()=>c?c(n.rawNode):O(this.title),trigger:()=>h(fe,{tmNode:n,clsPrefix:e,paddingLeft:this.paddingLeft,iconMarginRight:this.iconMarginRight,maxIconSize:this.maxIconSize,activeIconSize:this.activeIconSize,selected:this.selected,title:this.title,extra:this.extra,disabled:this.mergedDisabled,icon:this.icon,onClick:this.handleClick})}))}}),ge=Object.assign(Object.assign({},re),{tmNode:{type:Object,required:!0},tmNodes:{type:Array,required:!0}}),Ze=ee(ge),Qe=T({name:"MenuOptionGroup",props:ge,setup(e){L(oe,null);const r=ne(e);L(te,{paddingLeftRef:r.paddingLeft});const{mergedClsPrefixRef:n,props:c}=F($);return function(){const{value:a}=n,d=r.paddingLeft.value,{nodeProps:s}=c,f=s==null?void 0:s(e.tmNode.rawNode);return h("div",{class:`${a}-menu-item-group`,role:"group"},h("div",Object.assign({},f,{class:[`${a}-menu-item-group-title`,f==null?void 0:f.class],style:[(f==null?void 0:f.style)||"",d!==void 0?`padding-left: ${d}px;`:""]}),O(e.title),e.extra?h(Ee,null," ",O(e.extra)):null),h("div",null,e.tmNodes.map(u=>ie(u,c))))}}});function X(e){return e.type==="divider"||e.type==="render"}function Xe(e){return e.type==="divider"}function ie(e,r){const{rawNode:n}=e,{show:c}=n;if(c===!1)return null;if(X(n))return Xe(n)?h(qe,Object.assign({key:e.key},n.props)):null;const{labelField:a}=r,{key:d,level:s,isGroup:f}=e,u=Object.assign(Object.assign({},n),{title:n.title||n[a],extra:n.titleExtra||n.extra,key:d,internalKey:d,level:s,root:s===0,isGroup:f});return e.children?e.isGroup?h(Qe,J(u,Ze,{tmNode:e,tmNodes:e.children,key:d})):h(Y,J(u,Ye,{key:d,rawNodes:n[r.childrenField],tmNodes:e.children,tmNode:e})):h(Je,J(u,We,{key:d,tmNode:e}))}const xe=Object.assign(Object.assign({},re),{rawNodes:{type:Array,default:()=>[]},tmNodes:{type:Array,default:()=>[]},tmNode:{type:Object,required:!0},disabled:Boolean,icon:Function,onClick:Function,domId:String,virtualChildActive:{type:Boolean,default:void 0},isEllipsisPlaceholder:Boolean}),Ye=ee(xe),Y=T({name:"Submenu",props:xe,setup(e){const r=ne(e),{NMenu:n,NSubmenu:c}=r,{props:a,mergedCollapsedRef:d,mergedThemeRef:s}=n,f=b(()=>{const{disabled:m}=e;return c!=null&&c.mergedDisabledRef.value||a.disabled?!0:m}),u=M(!1);L(oe,{paddingLeftRef:r.paddingLeft,mergedDisabledRef:f}),L(te,null);function I(){const{onClick:m}=e;m&&m()}function N(){f.value||(d.value||n.toggleExpand(e.internalKey),I())}function y(m){u.value=m}return{menuProps:a,mergedTheme:s,doSelect:n.doSelect,inverted:n.invertedRef,isHorizontal:n.isHorizontalRef,mergedClsPrefix:n.mergedClsPrefixRef,maxIconSize:r.maxIconSize,activeIconSize:r.activeIconSize,iconMarginRight:r.iconMarginRight,dropdownPlacement:r.dropdownPlacement,dropdownShow:u,paddingLeft:r.paddingLeft,mergedDisabled:f,mergedValue:n.mergedValueRef,childActive:Q(()=>{var m;return(m=e.virtualChildActive)!==null&&m!==void 0?m:n.activePathRef.value.includes(e.internalKey)}),collapsed:b(()=>a.mode==="horizontal"?!1:d.value?!0:!n.mergedExpandedKeysRef.value.includes(e.internalKey)),dropdownEnabled:b(()=>!f.value&&(a.mode==="horizontal"||d.value)),handlePopoverShowChange:y,handleClick:N}},render(){var e;const{mergedClsPrefix:r,menuProps:{renderIcon:n,renderLabel:c}}=this,a=()=>{const{isHorizontal:s,paddingLeft:f,collapsed:u,mergedDisabled:I,maxIconSize:N,activeIconSize:y,title:m,childActive:w,icon:A,handleClick:R,menuProps:{nodeProps:P},dropdownShow:H,iconMarginRight:G,tmNode:K,mergedClsPrefix:j,isEllipsisPlaceholder:q,extra:B}=this,S=P==null?void 0:P(K.rawNode);return h("div",Object.assign({},S,{class:[`${j}-menu-item`,S==null?void 0:S.class],role:"menuitem"}),h(fe,{tmNode:K,paddingLeft:f,collapsed:u,disabled:I,iconMarginRight:G,maxIconSize:N,activeIconSize:y,title:m,extra:B,showArrow:!s,childActive:w,clsPrefix:j,icon:A,hover:H,onClick:R,isEllipsisPlaceholder:q}))},d=()=>h(Fe,null,{default:()=>{const{tmNodes:s,collapsed:f}=this;return f?null:h("div",{class:`${r}-submenu-children`,role:"menu"},s.map(u=>ie(u,this.menuProps)))}});return this.root?h(je,Object.assign({size:"large",trigger:"hover"},(e=this.menuProps)===null||e===void 0?void 0:e.dropdownProps,{themeOverrides:this.mergedTheme.peerOverrides.Dropdown,theme:this.mergedTheme.peers.Dropdown,builtinThemeOverrides:{fontSizeLarge:"14px",optionIconSizeLarge:"18px"},value:this.mergedValue,disabled:!this.dropdownEnabled,placement:this.dropdownPlacement,keyField:this.menuProps.keyField,labelField:this.menuProps.labelField,childrenField:this.menuProps.childrenField,onUpdateShow:this.handlePopoverShowChange,options:this.rawNodes,onSelect:this.doSelect,inverted:this.inverted,renderIcon:n,renderLabel:c}),{default:()=>h("div",{class:`${r}-submenu`,role:"menu","aria-expanded":!this.collapsed,id:this.domId},a(),this.isHorizontal?null:d())}):h("div",{class:`${r}-submenu`,role:"menu","aria-expanded":!this.collapsed,id:this.domId},a(),d())}}),eo=Object.assign(Object.assign({},he.props),{options:{type:Array,default:()=>[]},collapsed:{type:Boolean,default:void 0},collapsedWidth:{type:Number,default:48},iconSize:{type:Number,default:20},collapsedIconSize:{type:Number,default:24},rootIndent:Number,indent:{type:Number,default:32},labelField:{type:String,default:"label"},keyField:{type:String,default:"key"},childrenField:{type:String,default:"children"},disabledField:{type:String,default:"disabled"},defaultExpandAll:Boolean,defaultExpandedKeys:Array,expandedKeys:Array,value:[String,Number],defaultValue:{type:[String,Number],default:null},mode:{type:String,default:"vertical"},watchProps:{type:Array,default:void 0},disabled:Boolean,show:{type:Boolean,default:!0},inverted:Boolean,"onUpdate:expandedKeys":[Function,Array],onUpdateExpandedKeys:[Function,Array],onUpdateValue:[Function,Array],"onUpdate:value":[Function,Array],expandIcon:Function,renderIcon:Function,renderLabel:Function,renderExtra:Function,dropdownProps:Object,accordion:Boolean,nodeProps:Function,dropdownPlacement:{type:String,default:"bottom"},responsive:Boolean,items:Array,onOpenNamesChange:[Function,Array],onSelect:[Function,Array],onExpandedNamesChange:[Function,Array],expandedNames:Array,defaultExpandedNames:Array}),co=T({name:"Menu",inheritAttrs:!1,props:eo,setup(e){const{mergedClsPrefixRef:r,inlineThemeDisabled:n}=Me(e),c=he("Menu","-menu",Ge,Oe,e,r),a=F(Ue,null),d=b(()=>{var i;const{collapsed:v}=e;if(v!==void 0)return v;if(a){const{collapseModeRef:o,collapsedRef:l}=a;if(o.value==="width")return(i=l.value)!==null&&i!==void 0?i:!1}return!1}),s=b(()=>{const{keyField:i,childrenField:v,disabledField:o}=e;return Z(e.items||e.options,{getIgnored(l){return X(l)},getChildren(l){return l[v]},getDisabled(l){return l[o]},getKey(l){var C;return(C=l[i])!==null&&C!==void 0?C:l.name}})}),f=b(()=>new Set(s.value.treeNodes.map(i=>i.key))),{watchProps:u}=e,I=M(null);u!=null&&u.includes("defaultValue")?de(()=>{I.value=e.defaultValue}):I.value=e.defaultValue;const N=se(e,"value"),y=ue(N,I),m=M([]),w=()=>{m.value=e.defaultExpandAll?s.value.getNonLeafKeys():e.defaultExpandedNames||e.defaultExpandedKeys||s.value.getPath(y.value,{includeSelf:!1}).keyPath};u!=null&&u.includes("defaultExpandedKeys")?de(w):w();const A=Be(e,["expandedNames","expandedKeys"]),R=ue(A,m),P=b(()=>s.value.treeNodes),H=b(()=>s.value.getPath(y.value).keyPath);L($,{props:e,mergedCollapsedRef:d,mergedThemeRef:c,mergedValueRef:y,mergedExpandedKeysRef:R,activePathRef:H,mergedClsPrefixRef:r,isHorizontalRef:b(()=>e.mode==="horizontal"),invertedRef:se(e,"inverted"),doSelect:G,toggleExpand:j});function G(i,v){const{"onUpdate:value":o,onUpdateValue:l,onSelect:C}=e;l&&_(l,i,v),o&&_(o,i,v),C&&_(C,i,v),I.value=i}function K(i){const{"onUpdate:expandedKeys":v,onUpdateExpandedKeys:o,onExpandedNamesChange:l,onOpenNamesChange:C}=e;v&&_(v,i),o&&_(o,i),l&&_(l,i),C&&_(C,i),m.value=i}function j(i){const v=Array.from(R.value),o=v.findIndex(l=>l===i);if(~o)v.splice(o,1);else{if(e.accordion&&f.value.has(i)){const l=v.findIndex(C=>f.value.has(C));l>-1&&v.splice(l,1)}v.push(i)}K(v)}const q=i=>{const v=s.value.getPath(i??y.value,{includeSelf:!1}).keyPath;if(!v.length)return;const o=Array.from(R.value),l=new Set([...o,...v]);e.accordion&&f.value.forEach(C=>{l.has(C)&&!v.includes(C)&&l.delete(C)}),K(Array.from(l))},B=b(()=>{const{inverted:i}=e,{common:{cubicBezierEaseInOut:v},self:o}=c.value,{borderRadius:l,borderColorHorizontal:C,fontSize:Ae,itemHeight:Ne,dividerColor:Pe}=o,t={"--n-divider-color":Pe,"--n-bezier":v,"--n-font-size":Ae,"--n-border-color-horizontal":C,"--n-border-radius":l,"--n-item-height":Ne};return i?(t["--n-group-text-color"]=o.groupTextColorInverted,t["--n-color"]=o.colorInverted,t["--n-item-text-color"]=o.itemTextColorInverted,t["--n-item-text-color-hover"]=o.itemTextColorHoverInverted,t["--n-item-text-color-active"]=o.itemTextColorActiveInverted,t["--n-item-text-color-child-active"]=o.itemTextColorChildActiveInverted,t["--n-item-text-color-child-active-hover"]=o.itemTextColorChildActiveInverted,t["--n-item-text-color-active-hover"]=o.itemTextColorActiveHoverInverted,t["--n-item-icon-color"]=o.itemIconColorInverted,t["--n-item-icon-color-hover"]=o.itemIconColorHoverInverted,t["--n-item-icon-color-active"]=o.itemIconColorActiveInverted,t["--n-item-icon-color-active-hover"]=o.itemIconColorActiveHoverInverted,t["--n-item-icon-color-child-active"]=o.itemIconColorChildActiveInverted,t["--n-item-icon-color-child-active-hover"]=o.itemIconColorChildActiveHoverInverted,t["--n-item-icon-color-collapsed"]=o.itemIconColorCollapsedInverted,t["--n-item-text-color-horizontal"]=o.itemTextColorHorizontalInverted,t["--n-item-text-color-hover-horizontal"]=o.itemTextColorHoverHorizontalInverted,t["--n-item-text-color-active-horizontal"]=o.itemTextColorActiveHorizontalInverted,t["--n-item-text-color-child-active-horizontal"]=o.itemTextColorChildActiveHorizontalInverted,t["--n-item-text-color-child-active-hover-horizontal"]=o.itemTextColorChildActiveHoverHorizontalInverted,t["--n-item-text-color-active-hover-horizontal"]=o.itemTextColorActiveHoverHorizontalInverted,t["--n-item-icon-color-horizontal"]=o.itemIconColorHorizontalInverted,t["--n-item-icon-color-hover-horizontal"]=o.itemIconColorHoverHorizontalInverted,t["--n-item-icon-color-active-horizontal"]=o.itemIconColorActiveHorizontalInverted,t["--n-item-icon-color-active-hover-horizontal"]=o.itemIconColorActiveHoverHorizontalInverted,t["--n-item-icon-color-child-active-horizontal"]=o.itemIconColorChildActiveHorizontalInverted,t["--n-item-icon-color-child-active-hover-horizontal"]=o.itemIconColorChildActiveHoverHorizontalInverted,t["--n-arrow-color"]=o.arrowColorInverted,t["--n-arrow-color-hover"]=o.arrowColorHoverInverted,t["--n-arrow-color-active"]=o.arrowColorActiveInverted,t["--n-arrow-color-active-hover"]=o.arrowColorActiveHoverInverted,t["--n-arrow-color-child-active"]=o.arrowColorChildActiveInverted,t["--n-arrow-color-child-active-hover"]=o.arrowColorChildActiveHoverInverted,t["--n-item-color-hover"]=o.itemColorHoverInverted,t["--n-item-color-active"]=o.itemColorActiveInverted,t["--n-item-color-active-hover"]=o.itemColorActiveHoverInverted,t["--n-item-color-active-collapsed"]=o.itemColorActiveCollapsedInverted):(t["--n-group-text-color"]=o.groupTextColor,t["--n-color"]=o.color,t["--n-item-text-color"]=o.itemTextColor,t["--n-item-text-color-hover"]=o.itemTextColorHover,t["--n-item-text-color-active"]=o.itemTextColorActive,t["--n-item-text-color-child-active"]=o.itemTextColorChildActive,t["--n-item-text-color-child-active-hover"]=o.itemTextColorChildActiveHover,t["--n-item-text-color-active-hover"]=o.itemTextColorActiveHover,t["--n-item-icon-color"]=o.itemIconColor,t["--n-item-icon-color-hover"]=o.itemIconColorHover,t["--n-item-icon-color-active"]=o.itemIconColorActive,t["--n-item-icon-color-active-hover"]=o.itemIconColorActiveHover,t["--n-item-icon-color-child-active"]=o.itemIconColorChildActive,t["--n-item-icon-color-child-active-hover"]=o.itemIconColorChildActiveHover,t["--n-item-icon-color-collapsed"]=o.itemIconColorCollapsed,t["--n-item-text-color-horizontal"]=o.itemTextColorHorizontal,t["--n-item-text-color-hover-horizontal"]=o.itemTextColorHoverHorizontal,t["--n-item-text-color-active-horizontal"]=o.itemTextColorActiveHorizontal,t["--n-item-text-color-child-active-horizontal"]=o.itemTextColorChildActiveHorizontal,t["--n-item-text-color-child-active-hover-horizontal"]=o.itemTextColorChildActiveHoverHorizontal,t["--n-item-text-color-active-hover-horizontal"]=o.itemTextColorActiveHoverHorizontal,t["--n-item-icon-color-horizontal"]=o.itemIconColorHorizontal,t["--n-item-icon-color-hover-horizontal"]=o.itemIconColorHoverHorizontal,t["--n-item-icon-color-active-horizontal"]=o.itemIconColorActiveHorizontal,t["--n-item-icon-color-active-hover-horizontal"]=o.itemIconColorActiveHoverHorizontal,t["--n-item-icon-color-child-active-horizontal"]=o.itemIconColorChildActiveHorizontal,t["--n-item-icon-color-child-active-hover-horizontal"]=o.itemIconColorChildActiveHoverHorizontal,t["--n-arrow-color"]=o.arrowColor,t["--n-arrow-color-hover"]=o.arrowColorHover,t["--n-arrow-color-active"]=o.arrowColorActive,t["--n-arrow-color-active-hover"]=o.arrowColorActiveHover,t["--n-arrow-color-child-active"]=o.arrowColorChildActive,t["--n-arrow-color-child-active-hover"]=o.arrowColorChildActiveHover,t["--n-item-color-hover"]=o.itemColorHover,t["--n-item-color-active"]=o.itemColorActive,t["--n-item-color-active-hover"]=o.itemColorActiveHover,t["--n-item-color-active-collapsed"]=o.itemColorActiveCollapsed),t}),S=n?Ke("menu",b(()=>e.inverted?"a":"b"),B,e):void 0,W=ke(),le=M(null),Ce=M(null);let ae=!0;const ce=()=>{var i;ae?ae=!1:(i=le.value)===null||i===void 0||i.sync({showAllItemsBeforeCalculate:!0})};function be(){return document.getElementById(W)}const V=M(-1);function ze(i){V.value=e.options.length-i}function ye(i){i||(V.value=-1)}const Ie=b(()=>{const i=V.value;return{children:i===-1?[]:e.options.slice(i)}}),we=b(()=>{const{childrenField:i,disabledField:v,keyField:o}=e;return Z([Ie.value],{getIgnored(l){return X(l)},getChildren(l){return l[i]},getDisabled(l){return l[v]},getKey(l){var C;return(C=l[o])!==null&&C!==void 0?C:l.name}})}),Re=b(()=>Z([{}]).treeNodes[0]);function Se(){var i;if(V.value===-1)return h(Y,{root:!0,level:0,key:"__ellpisisGroupPlaceholder__",internalKey:"__ellpisisGroupPlaceholder__",title:"···",tmNode:Re.value,domId:W,isEllipsisPlaceholder:!0});const v=we.value.treeNodes[0],o=H.value,l=!!(!((i=v.children)===null||i===void 0)&&i.some(C=>o.includes(C.key)));return h(Y,{level:0,root:!0,key:"__ellpisisGroup__",internalKey:"__ellpisisGroup__",title:"···",virtualChildActive:l,tmNode:v,domId:W,rawNodes:v.rawNode.children||[],tmNodes:v.children||[],isEllipsisPlaceholder:!0})}return{mergedClsPrefix:r,controlledExpandedKeys:A,uncontrolledExpanededKeys:m,mergedExpandedKeys:R,uncontrolledValue:I,mergedValue:y,activePath:H,tmNodes:P,mergedTheme:c,mergedCollapsed:d,cssVars:n?void 0:B,themeClass:S==null?void 0:S.themeClass,overflowRef:le,counterRef:Ce,updateCounter:()=>{},onResize:ce,onUpdateOverflow:ye,onUpdateCount:ze,renderCounter:Se,getCounter:be,onRender:S==null?void 0:S.onRender,showOption:q,deriveResponsiveState:ce}},render(){const{mergedClsPrefix:e,mode:r,themeClass:n,onRender:c}=this;c==null||c();const a=()=>this.tmNodes.map(u=>ie(u,this.$props)),s=r==="horizontal"&&this.responsive,f=()=>h("div",Le(this.$attrs,{role:r==="horizontal"?"menubar":"menu",class:[`${e}-menu`,n,`${e}-menu--${r}`,s&&`${e}-menu--responsive`,this.mergedCollapsed&&`${e}-menu--collapsed`],style:this.cssVars}),s?h(Ve,{ref:"overflowRef",onUpdateOverflow:this.onUpdateOverflow,getCounter:this.getCounter,onUpdateCount:this.onUpdateCount,updateCounter:this.updateCounter,style:{width:"100%",display:"flex",overflow:"hidden"}},{default:a,counter:this.renderCounter}):a());return s?h(Te,{onResize:this.onResize},{default:f}):f()}});export{co as _};

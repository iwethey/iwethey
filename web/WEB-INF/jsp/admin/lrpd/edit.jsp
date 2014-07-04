<%--
   Copyright 2004-2010 Scott Anderson and Mike Vitale

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
--%>

<%@ include file="../../common.jsp" %>

<c:set var="admin" value="true" />

<%@ include file="../../header.jsp" %>

<script type="text/javascript">
	Ext.onReady(function() {
		// shorthand alias
		var fm = Ext.form;

		// the column model has information about grid columns
		// dataIndex maps the column to the specific data field in
		// the data store (created below)
		var cm = new Ext.grid.ColumnModel({
			// specify any defaults for each column
			defaults: {
				sortable: true // columns are not sortable by default
			},
			columns: [
				{
					id: 'id',
					header: 'ID',
					dataIndex: 'id',
					width: 100,
					editable : false
				}, {
					header: 'Quote',
					dataIndex: 'quote',
					width: 400,
					editor: new fm.TextField({
						allowBlank: false
					})
				}]
		});

		// create the Data Store
		var store = new Ext.data.Store({
			// destroy the store if the grid is destroyed
			autoDestroy: true,

			// load remote data using HTTP
			url: '<c:url value="/admin/lrpd/allLrpds.json" />',

			// specify a XmlReader (coincides with the XML format of the returned data)
			reader: new Ext.data.JsonReader({
				// records will have a 'quote' tag
				record: 'quote',
				// use an Array of field definition objects to implicitly create a Record constructor
				fields: [
					// the 'name' below matches the tag name to read, except 'availDate'
					// which is mapped to the tag 'availability'
					{name: 'id', type: 'int'},
					{name: 'quote', type: 'string'}
				]
			}),

			sortInfo: {field:'id', direction:'ASC'}
		});

		var grid = new Ext.grid.EditorGridPanel({
			store : store,
			cm : cm,
			renderTo : 'editor-grid',
			width : 600,
			height : 300,
			//autoExpandColumn : 'Quote', // column with this id will be expanded
			title : 'Edit LRPDs',
			frame : true,
			// specify the check column plugin on the grid so the plugin is initialized
			//plugins : checkColumn,
			clicksToEdit : 1
		});

		// manually trigger the data store load
		store.load({
			// store loading is asynchronous, use a load listener or callback to handle results
			callback: function() {
				Ext.Msg.show({
					title: 'Store Load Callback',
					msg: 'store was loaded, data available for processing',
					modal: false,
					icon: Ext.Msg.INFO,
					buttons: Ext.Msg.OK
				});
			}
		});

		Ext.Msg.show({
			title: 'Loaded',
			msg: 'Page Loaded',
			modal: false,
			icon: Ext.Msg.INFO,
			buttons: Ext.Msg.OK
		});

	});
</script>

<h1><fmt:message key="admin"/></h1>

<h1>EDIT EL ARRR PEE DEE</h1>


<div id="editor-grid"></div>

<%@ include file="../../footer.jsp" %>

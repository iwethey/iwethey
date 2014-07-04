Ext.onReady(function() {
	function formatDate(value) {
		return value ? value.dateFormat('M d, Y') : '';
	}

	// shorthand alias
	var fm = Ext.form;

    // the check column is created using a custom plugin
    var checkColumn = new Ext.grid.CheckColumn({
       header: 'Approved?',
       dataIndex: 'approved',
       width: 100
    });

	// the column model has information about grid columns
	// dataIndex maps the column to the specific data field in
	// the data store (created below)
	var cm = new Ext.grid.ColumnModel({
		// specify any defaults for each column
		defaults : {
			sortable: true // columns are not sortable by default
		},
		columns : [
			{
				id: 'id',
				header: 'ID',
				dataIndex: 'id',
				width: 75,
				editable : false
			}, {
				header: 'Quote',
				dataIndex: 'quote',
				width: 844,
				editor: new fm.TextField({
					allowBlank: false
				})
			},
			checkColumn
		]
	});

	// create the Data Store
	var store = new Ext.data.Store({
		// destroy the store if the grid is destroyed
		autoDestroy: true,

		// load remote data using HTTP
		url: dynamic.baseUri + "admin/lrpd/allLrpds.json",

		// specify a XmlReader (coincides with the XML format of the returned data)
		reader : new Ext.data.JsonReader({
			root : 'quotes',
			// records will have a 'quote' tag
			record : '',
			// use an Array of field definition objects to implicitly create a Record constructor
			fields : [
				// the 'name' below matches the tag name to read, except 'availDate'
				// which is mapped to the tag 'availability'
				{name: 'id', type: 'int'},
				{name: 'quote', type: 'string'},
				{name: 'approved', type: 'bool'}
			]
		}),

		sortInfo : {
			field     : 'id',
			direction : 'ASC'
		}
	});

	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		cm : cm,
		renderTo : 'editor-grid',
		width : 1050,
		height : 300,
		//autoExpandColumn : 'quote', // column with this id will be expanded
		title : 'Edit LRPDs',
		frame : true,
		// specify the check column plugin on the grid so the plugin is initialized
		//plugins : checkColumn,
		clicksToEdit : 1
	});

	// manually trigger the data store load
	store.load({ });

	grid.on('afteredit', afterEdit, this );

	function afterEdit(e) {
		//Ext.Msg.alert('blar', e.record.quote);
		Ext.Ajax.request({
			url : dynamic.baseUri + 'admin/lrpd/saveLrpd.json',
			params : {
				id       : e.record.data.id,
				approved : e.record.data.approved,
				quote    : e.record.data.quote
			}
		});
		// execute an XHR to send/commit data to the server, in callback do (if successful):
		//e.record.commit();

	};
});

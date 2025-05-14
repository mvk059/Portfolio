
var operations = ["+", "-", "/", "*"];
// Instance vars
var borders = [];
var groups = [];
var rows = [];
var cells = [];
var group_limit;
var hoverEl = null;

$(document).ready(function() {
    generate_board();
});

/*******************************************
 *  Cell and Group objects
 *******************************************/
function Cell(row, col, index) {
    this.row = row;
    this.col = col;
    this.index = index;
    this.group = null;
    this.sol = function(solution) {
        return solution[this.row][this.col];
    };
    this.possible = [];
    this.addPossibleChoices = function(arr) {
        for (var i = 0; i < arr.length; i++) {
            if (jQuery.inArray(arr[i], this.possible) == -1) {
                this.possible.push(arr[i]);
            }
        }
    };
    // group related bits

    this.border_top = false;
    this.border_right = false;
    // UI
    var handleMouse = function(e, type) {
        var el = e.currentTarget;
        if (type == "mousemove" || type == "mouseout") {
            $(hoverEl).removeClass("hover-border-left hover-border-right hover-border-top hover-border-bottom");
            hoverEl = el;
        }
        var parts = $(el).attr('id').split('c');
        var row = parseInt(parts[1]);
        var col = parseInt(parts[2]);

        // get click position
        var offset = $(el).offset();
        var div_x = offset.left;
        var div_y = offset.top;
        var size_x = $(el).width();
        var size_y = $(el).height();
        var click_pos_x = parseInt(e.pageX) - parseInt(div_x);
        var click_pos_y = parseInt(e.pageY) - parseInt(div_y);

        if (click_pos_x > click_pos_y) {
            if (click_pos_y > -click_pos_x + size_x) {
                //right
                if (col < rows.length) {
                    if (type == "click") {
                        rows[row][col].border_right = !rows[row][col].border_right;
                    } else if (type == "mousemove") {
                        $(hoverEl).addClass("hover-border-right");
                    }
                }
            } else {
                //top
                if (row > 0) {
                    if (type == "click") {
                        rows[row][col].border_top = !rows[row][col].border_top;
                    } else if (type == "mousemove") {
                        $(hoverEl).addClass("hover-border-top");
                    }
                }
            }
        } else {
            // Transfer to cell below/to the left
            if (click_pos_y > -click_pos_x + size_x) {
                //bottom
                if (row < rows.length) {
                    if (type == "click") {
                        rows[row + 1][col].border_top = !rows[row + 1][col].border_top;
                    } else if (type == "mousemove") {
                        $(hoverEl).addClass("hover-border-bottom");
                    }
                }
            } else {
                //left
                if (col > 0) {
                    if (type == "click") {
                        rows[row][col - 1].border_right = !rows[row][col - 1].border_right;
                    } else if (type == "mousemove") {
                        $(hoverEl).addClass("hover-border-left");
                    }
                }
            }
        }
        if (type == "click") {
            update_groups();
        }
    }
    this.div = $(document.createElement("div")).attr({
        class: "cell",
        id: "c" + row + "c" + col
    }).bind("click", function(e) {
        handleMouse(e, "click")
    }).bind("mousemove", function(e) {
        handleMouse(e, "mousemove");
    }).bind("mouseout", function(e) {
        handleMouse(e, "mouseout");
    });

    this.rule_div = $(document.createElement("div"));
    this.value_div = $(document.createElement("div"));

    this.div.append(this.rule_div);
    this.div.append(this.value_div);
}

function Group() {
    this.goal = 0;
    this.index = -1;
    this.operation = "+";
    // The cell containing the description.
    this.topmost = null;
    this.members = [];
    this.add = function(cell) {
        cell.group = this;
        this.members.push(cell);
    }
    this.addAll = function(all) {
        for (var i = 0; i < all.length; i++) {
            this.add(all[i]);
        }
    }
}

/*******************************************
 *  Drawing board, creating data structures
 *******************************************/
var MAX_SIZE = 12;
var MAX_SANE_SIZE = 9;

function generate_board() {
    $("#solved").html("");
    var size = parseInt($('#boardsize').val());
    if (size > MAX_SANE_SIZE) {
        alert("Watch out.  Sizes over " + MAX_SANE_SIZE + " can get really slow, and might crash your browser.");
    } else if (size > MAX_SIZE) {
        alert("Maximum size " + MAX_SIZE + ".");
        $('#boardsize').val(MAX_SIZE);
        size = MAX_SIZE;
    } else if (size < 1) {
        alert("Minimum size 1.");
        $('#boardsize').val(1);
        size = 1;
    }
    var board = $('#kenken');
    board.html("");

    // reset borders
    borders = [];
    for (var row = 0; row < size + 1; row++) {
        borders.push([])
        for (var col = 0; col < size + 1; col++) {
            borders[row].push(false);
        }
    }

    // reset rows
    rows = [];
    cells = [];
    groups = [];
    for (var row = 0; row < size; row++) {
        rows.push([]);
        var rowdiv = $(document.createElement('div'));
        rowdiv.addClass('row');
        board.append(rowdiv);
        for (var col = 0; col < size; col++) {
            var i = row * size + col;
            var cell = new Cell(row, col, i);
            rows[row][col] = cell;
            cells[i] = cell;
            rowdiv.append(cell.div);
        }
    }
    update_groups();
}
function random_puzzle() {
    group_limit = parseInt($("#grouplimit").val());
    generate_board();
    for (var c = 0; c < cells.length; c++) {
        cells[c].group = null;
    }
    groups = [];

    // create groups
    var visited = []
    for (var c = 0; c < cells.length; c++) {
        var cell = cells[c];
        if (cell.group === null) {
            // add the rows to the group
            var g = new Group();
            g.topmost = cell;
            g.add(cell);
            var g_cell = cell;
            while (g.members.length < group_limit) {
                var r = Math.random();
                var neighbors = [];
                var addNeighbor = function(row, col) {
                    try {
                        var neighbor = rows[g_cell.row + row][g_cell.col + col];
                        if (neighbor.group == null) {
                            neighbors.push(neighbor);
                        }
                    } catch (err) {}
                };
                addNeighbor(0, -1); // left
                addNeighbor(0, 1); // right
                addNeighbor(1, 0); // top
                addNeighbor(-1, 0); // bottom

                if (neighbors.length == 0 || Math.random() < 0.3) {
                    break;
                }
                var pick = neighbors[Math.floor(Math.random() * neighbors.length)];
                g.add(pick);
                g_cell = pick;
            }
            // update borders
            for (var i = 0; i < g.members.length; i++) {
                var m = g.members[i];
                if (m.row > 0 && rows[m.row - 1][m.col].group != g) {
                    m.border_top = true;
                }
                if (m.col < rows.length - 1 && rows[m.row][m.col + 1].group != g) {
                    m.border_right = true;
                }
            }
            groups.push(g);
        }
    }
    //
    // Make targets.
    //
    // make a vanilla solution
    var solution = [];
    var c = 0;
    for (var i = 0; i < rows.length; i++) {
        solution.push([]);
        for (var j = 1; j < rows.length + 1; j++) {
            solution[i].push(((c + i) % rows.length) + 1);
            c++;
        }
    }
    // randomize the rows
    for (var i = 0; i < 10; i++) {
        var from_row = Math.floor(Math.random() * rows.length);
        var to_row = Math.floor(Math.random() * rows.length);
        var temp = solution[from_row];
        solution[from_row] = solution[to_row];
        solution[to_row] = temp;
    }
    // randomize the columns
    for (var i = 0; i < 10; i++) {
        var from_col = Math.floor(Math.random() * rows.length);
        var to_col = Math.floor(Math.random() * rows.length);
        var temp;
        for (var j = 0; j < rows.length; j++) {
            temp = solution[j][from_col];
            solution[j][from_col] = solution[j][to_col];
            solution[j][to_col] = temp;
        }
    }

    // Choose an operation and calculate target
    var group_solutions = function(group) {
        var sols = [];
        for (var i = 0; i < group.members.length; i++) {
            sols.push(solution[group.members[i].row][group.members[i].col]);
        }
        return sols;
    };
    for (var i = 0; i < groups.length; i++) {
        var g = groups[i];
        var s = group_solutions(g);
        if (g.members.length === 1) {
            g.operation = "+";
        } else {
            var possible = ["+", "*"];
            if (g.members.length === 2) {
                possible.push("-");
                if ((s[0] / s[1]) == Math.floor(s[0] / s[1]) ||
                    (s[1] / s[0]) == Math.floor(s[1] / s[0])) {
                    possible.push("/");
                }
            }
            g.operation = possible[Math.floor(Math.random() * possible.length)];
        }
        if (g.operation == "-") {
            g.goal = (s[0] - s[1]) > 0 ? (s[0] - s[1]) : (s[1] - s[0]);
        } else if (g.operation == "/") {
            if (s[0] / s[1] == Math.floor(s[0] / s[1])) {
                g.goal = s[0] / s[1];
            } else {
                g.goal = s[1] / s[0];
            }
        } else if (g.operation == "*") {
            g.goal = 1;
            for (var j = 0; j < s.length; j++) {
                g.goal *= s[j];
            }
        } else if (g.operation == "+") {
            g.goal = 0;
            for (var j = 0; j < s.length; j++) {
                g.goal += s[j];
            }
        }
    }

    //draw_solution(solution);
    // update css
    update_borders();
    // update topmost points
    update_rule_divs();
}

// Finds the cells that are in a particular cell's group (that is, no walls
// separate them)
function find_neighbors(cell, neighbors) {
    // banking on the fact that all top-row cells have a top border
    if (!neighbors) {
        neighbors = [cell];
    }
    if (!cell.border_top && cell.row > 0) {
        _find_neighbors_push(rows[cell.row - 1][cell.col], neighbors);
    }
    if (!cell.border_right && cell.col < rows.length - 1) {
        _find_neighbors_push(rows[cell.row][cell.col + 1], neighbors);
    }
    if (cell.row < rows.length - 1) {
        var below = rows[cell.row + 1][cell.col];
        if (!below.border_top) {
            _find_neighbors_push(below, neighbors);
        }
    }
    if (cell.col > 0) {
        var left = rows[cell.row][cell.col - 1];
        if (!left.border_right) {
            _find_neighbors_push(left, neighbors);
        }
    }
    return neighbors;
}
function _find_neighbors_push(cell, neighbors) {
    if (jQuery.inArray(cell, neighbors) == -1) {
        neighbors.push(cell);
        find_neighbors(cell, neighbors);
    }
}

// Updates the group data structure to reflect the current cells.  Called after
// mouse-clicks on the board change the group structure or on board generation.
function update_groups() {
    var old_groups = groups;
    groups = [];
    // get the list of groups from the UI.
    var g = new Group();
    var visited = [];
    for (var i = 0; i < cells.length; i++) {
        var cell = cells[i];
        if (jQuery.inArray(cell, visited) == -1) {
            var neighbors = find_neighbors(cell, null);
            g.addAll(neighbors);
            g.topmost = cell;
            g.index = groups.length;
            groups.push(g);
            for (var n = 0; n < neighbors.length; n++) {
                neighbors[n].group = g;
            }
            visited = jQuery.merge(visited, neighbors);
            // start again for next group
            g = new Group();
        }
    }
    var num_groups = Math.min(old_groups.length, groups.length);
    for (var i = 0; i < num_groups; i++) {
        groups[i].goal = old_groups[i].goal;
        groups[i].operation = old_groups[i].operation;
    }
    //update css
    update_borders();
    //update topmost points
    update_rule_divs();
}

// Draw the borders to reflect those in the cells.
function update_borders() {
    for (var row = 0; row < rows.length; row++) {
        for (var col = 0; col < rows[row].length; col++) {
            // Update cell border
            if (row == 0 || rows[row][col].border_top) {
                rows[row][col].div.addClass('border-top');
            } else {
                rows[row][col].div.removeClass('border-top');
            }
            if (col == rows[row].length - 1 || rows[row][col].border_right) {
                rows[row][col].div.addClass('border-right');
            } else if (col != rows[row].length) {
                rows[row][col].div.removeClass('border-right');
            }
            if (col == 0) {
                rows[row][col].div.addClass('border-left');
            } else {
                rows[row][col].div.removeClass('border-left');
            }
            if (row == rows.length - 1) {
                rows[row][col].div.addClass('border-bottom');
            } else {
                rows[row][col].div.removeClass('border-bottom');
            }
        }
    }
}

// Draw the inputs for group rules to reflect the current groups.
function update_rule_divs() {
    for (var i = 0; i < cells.length; i++) {
        var cell = cells[i];
        cell.rule_div.html("");
        if (cell.group.topmost == cell) {
            var rule = $(document.createElement("input")).attr({
                class: 'rule',
                size: 3,
                id: 'g' + cell.group.index,
                value: cell.group.goal + "" + cell.group.operation
            });
            rule.bind('change', function(e) {
                var g_id = parseInt($(this).attr('id').replace('g', ''));
                var val = $(this).val();
                if (!isNaN(val)) {
                    val += '+';
                }
                groups[g_id].goal = parseInt(val.substring(0, val.length - 1));
                groups[g_id].operation = val.substring(val.length - 1, val.length);
            }).bind('click', function(e) {
                e.stopPropagation();
            });
            cell.rule_div.append(rule);
        }
    }
}

/********************************
 *  Solving
 ********************************/
function groupsolve() {
    var start = new Date();
    get_all_group_solutions();
    groups = groups.sort(function(a, b) {
        if (a.possible.length < b.possible.length) {
            return -1;
        } else if (a.possible.length == b.possible.length) {
            return 0;
        }
        return 1;
    });

    var searchspace = 1;
    var group_nums = "";
    for (var i = 0; i < groups.length; i++) {
        searchspace *= groups[i].possible.length;
        group_nums += groups[i].possible.length + " ";
    }
    debug("");
    debug(group_nums);
    debug("Searchspace: " + searchspace);

    var gindices = [];
    var solution = [];
    for (var i = 0; i < rows.length; i++) {
        solution.push([]);
        for (var j = 0; j < rows.length; j++) {
            solution[i].push(0);
        }
    }
    var fill_solution = function() {
        for (var i = 0; i < gindices.length; i++) {
            var group = groups[i];
            for (var c = 0; c < group.members.length; c++) {
                var cell = group.members[c];
                solution[cell.row][cell.col] = group.possible[gindices[i]][c];
            }
        }
        for (var j = gindices.length; j < groups.length; j++) {
            var group = groups[j];
            for (var i = 0; i < group.members.length; i++) {
                var cell = group.members[i];
                solution[cell.row][cell.col] = 0;
            }
        }
    };
    var visited = new Set();
    var to_visit = new Stack();
    for (var i = 0; i < groups[0].possible.length; i++) {
        to_visit.push([i]);
    }
    var solved = false;
    var traversed = 0;
    while (to_visit.length > 0) {
        gindices = to_visit.pop();
        traversed += 1;
        fill_solution();
        if (gindices.length == groups.length) {
            if (checkall(solution)) {
                var solved = true;
                draw_solution(solution);
                break;
            } else {
                continue;
            }
        } else {
            if (!visited.contains(gindices) && check_rows(solution) && check_cols(solution)) {
                visited.add(gindices);
                var pos = gindices.length;
                gindices.push(0);
                for (var i = 0; i < groups[pos].possible.length; i++) {
                    gindices[pos] = i;
                    to_visit.push(gindices);
                }
            }
        }
    }
    var end = new Date();
    debug("Traversed: " + traversed + "(" + ((100 * traversed)/searchspace) + ")");
    debug("Time: " + ((end - start) / 1000.) + " seconds.");
    if (!solved) {
        alert("No solution!");
    } else {
        $("#solved").html("Solved in " + ((end - start) / 1000.) + " seconds.");
    }
}

function solve() {
    var start = new Date();

    construct_group_constrained_choices();
    // sort groups according to group size
    var sorted_cells = [];
    for (var i = 0; i < cells.length; i++) {
        sorted_cells.push(cells[i]);
    }
    sorted_cells.sort(function(a, b) {
        if (a.possible.length < b.possible.length) {
            return -1;
        } else if (a.possible.length == b.possible.length) {
            return 0;
        }
        return 1;
    });

    // indices of which of the possible solutions for each cell we are guessing.
    var indices = [];
    // the solution itself, in natural row/column order.
    var solution = [];
    for (var i = 0; i < rows.length; i++) {
        solution.push([]);
        for (var j = 0; j < rows.length; j++) {
            solution[i].push(0);
        }
    }
    // function to fill the solution from indicies to our sorted cell array
    // Fill 0 for any index we haven't yet added.
    function fill_solution() {
        for (var i = 0; i < indices.length; i++) {
            var cell = sorted_cells[i];
            solution[cell.row][cell.col] = cell.possible[indices[i]];
        }
        for (var j = indices.length; j < sorted_cells.length; j++) {
            var cell = sorted_cells[j];
            solution[cell.row][cell.col] = 0;
        }
    }
    indices.push(0);
    fill_solution();
    var visited = new Set();
    var to_visit = new Stack();
    to_visit.push(indices);
    var solved = false;
    while (to_visit.length > 0) {
        indices = to_visit.pop();
        fill_solution();
        if (indices.length == sorted_cells.length) {
            if (checkall(solution)) {
                var solved = true;
                draw_solution(solution);
                break;
            } else {
                continue;
            }
        } else {
            if (!visited.contains(indices) && check_rows(solution) && check_cols(solution)) {
                visited.add(indices);
                var pos = indices.length;
                indices.push(0);
                for (var i = 0; i < sorted_cells[pos].possible.length; i++) {
                    indices[pos] = i;
                    to_visit.push(indices);
                }
            }
        }
    }
    var end = new Date();
    if (!solved) {
        alert("No solution!");
    } else {
        $("#solved").html("Solved in " + ((end - start) / 1000.) + " seconds.");
    }
}
function copyArray(arr) {
    var copy = [];
    for (var i = 0; i < arr.length; i++) {
        copy.push(arr[i]);
    }
    return copy;
}
function Set() {
    var visited = [];
    this.add = function(thing) {
        if (!this.contains(thing)) {

            visited.push(copyArray(thing));
        }
    };
    this.contains = function(thing) {
        return jQuery.inArray(thing, visited) != -1;
    };
}
function Stack() {
    var stack = [];
    this.length = 0;
    this.push = function(arr) {
        stack.push(copyArray(arr));
        this.length += 1;
    }
    this.pop = function(arr) {
        this.length -= 1;
        return stack.pop();
    }
}
function copyNumSort(array) {
    var copy = array.slice();
    copy.sort(function(a, b) { return a < b ? -1 : (a > b ? 1 : 0) });
    return copy;
}
function get_all_group_solutions() {
    for (var g = 0; g < groups.length; g++) {
        var group = groups[g];
        var combine;
        if (group.operation == "-") {
            combine = function(choices) {
                choices = copyNumSort(choices);
                var val = choices[choices.length-1];
                for (var i = choices.length - 2; i >= 0; i--) {
                    val -= choices[i];
                }
                return val;
            }
        } else if (group.operation == "*") {
            combine = function(choices) {
                var total = 1;
                for (var i = 0; i < choices.length; i++) {
                    total *= choices[i];
                }
                return total;
            }
        } else if (group.operation == "/") {
            combine = function(choices) {
                choices = copyNumSort(choices);
                var val = choices[choices.length-1];
                for (var i = choices.length - 2; i >= 0; i--) {
                    val /= choices[i];
                }
                return val;
            }
        } else {
            combine = function(choices) {
                var total = 0;
                for (var i = 0; i < choices.length; i++) {
                    total += choices[i];
                }
                return total;
            }
        }
        var choices = [];
        var max = rows.length;
        var min = 1;
        for (var i = 0; i < group.members.length; i++) {
            choices.push(min);
        }
        var incomplete = 1;
        group.possible = [];
        var check_solution = function(choices) {
            if (combine(choices) != group.goal) {
                return false;
            }
            var solution = [];
            for (var i = 0; i < rows.length; i++) {
                solution.push([]);
                for (var j = 0; j < rows.length; j++) {
                    solution[i].push(0);
                }
            }
            for (var i = 0; i < choices.length; i++) {
                solution[group.members[i].row][group.members[i].col] = choices[i];
            }
            return check_rows(solution) && check_cols(solution);
        };
        while (incomplete) {
            if (check_solution(choices)) {
                group.possible.push(copyArray(choices));
            }
            var p = 0;
            choices[p] += 1;
            while (choices[p] > max) {
                choices[p] = min;
                p += 1;
                if (p >= choices.length) {
                    incomplete = 0;
                } else {
                    choices[p] += 1;
                }
            }
        }
    }

}
function construct_group_constrained_choices() {
    for (var g = 0; g < groups.length; g++) {
        var group = groups[g];
        var combine;
        if (group.operation == "-") {
            combine = function(choices) {
                choices = copyNumSort(choices);
                var total = choices[choices.length - 1];
                for (var i = choices.length - 2; i >= 0; i--) {
                    total -= choices[i];
                }
                return total;
            }
        } else if (group.operation == "*") {
            combine = function(choices) {
                var total = 1;
                for (var i = 0; i < choices.length; i++) {
                    total *= choices[i];
                }
                return total;
            }
        } else if (group.operation == "/") {
            combine = function(choices) {
                choices = copyNumSort(choices);
                var total = choices[choices.length - 1];
                for (var i = 0; i < choices.length; i++) {
                    total /= choices[i];
                }
                return total;
            }
        } else {
            combine = function(choices) {
                var total = 0;
                for (var i = 0; i < choices.length; i++) {
                    total += choices[i];
                }
                return total;
            }
        }
        var choices = [];
        var max = rows.length;
        var min = 1;
        for (var i = 0; i < group.members.length; i++) {
            choices.push(min);
        }
        var incomplete = 1;
        while (incomplete) {
            if (combine(choices) == group.goal) {
                for (var m = 0; m < group.members.length; m++) {
                    group.members[m].addPossibleChoices(choices);
                }
            }
            var p = 0;
            choices[p] += 1;
            while (choices[p] > max) {
                choices[p] = min;
                p += 1;
                if (p >= choices.length) {
                    incomplete = 0;
                } else {
                    choices[p] += 1;
                }
            }
        }
    }
}
function draw_solution(solution) {
    for (var i = 0; i < rows.length; i++) {
        for (var j = 0; j < rows.length; j++) {
            rows[i][j].div.append("<div class='sol'>" + solution[i][j] + "</div>");
        }
    }
}


/*********************************
 *  Checking puzzle for validity
 *********************************/

// Returns true if the puzzle is a valid solution, and false if it is not.
function checkall(solution) {
    return check_groups(solution) && check_cols(solution) && check_rows(solution);
}
function check_groups(solution) {
    for (var g = 0; g < groups.length; g++) {
        if (!check_group(groups[g], solution)) {
            return false;
        }
    }
    return true;
}
function check_group(group, solution) {
    if (group.members.length == 1) {
        return true;
    } else if (group.operation == '+') {
        var total = 0;
        for (var g = 0; g < group.members.length; g++) {
            total += group.members[g].sol(solution);
        }
        return total == group.goal;
    } else if (group.operation == '*') {
        var total = 1;
        for (var g = 0; g < group.members.length; g++) {
            total *= group.members[g].sol(solution);
        }
        return total == group.goal;
    } else {
        // For divide and subtract, arrange operands in descending order.
        var operands = [];
        for (var i = 0; i < group.members.length; i++) {
            operands.push(group.members[i].sol(solution));
        }
        operands = copyNumSort(operands);
        var val = operands[operands.length - 1];
        for (var i = operands.length - 2; i >= 0; i--) {
            if (group.operation == '-') {
                val -= operands[i];
            } else if (group.operation == '/') {
                val /= operands[i];
            }
        }
        return val == group.goal;
    }
    return false;
}
function check_rows(solution) {
    // check rows
    for (var row = 0; row < rows.length; row++) {
        var in_row = [];
        for (var col = 0; col < rows[row].length; col++) {
            var val = solution[row][col];
            if (val != 0 && jQuery.inArray(val, in_row) != -1) {
                return false;
            }
            in_row.push(val);
        }
    }
    return true;
}
function check_cols(solution) {
    // check cols
    for (var col = 0; col < rows.length; col++) {
        var in_col = [];
        for (var row = 0; row < rows.length; row++) {
            var val = solution[row][col];
            if (val != 0 && jQuery.inArray(val, in_col) != -1) {
                return false;
            }
            in_col.push(val);
        }
    }
    return true;
}

/******************************
 *  Debug
 ******************************/
function debug(stuff) {
    var d = $('#debug');
    d.html( d.html() + "<br />" + stuff );
}
function debug_obj(obj) {
    debug(obj);
    for (var i in obj) {
        debug(i + "=&gt;" + obj[i]);
    }
}
